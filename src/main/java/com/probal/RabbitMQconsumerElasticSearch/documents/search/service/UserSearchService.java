package com.probal.RabbitMQconsumerElasticSearch.documents.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.documents.helper.Indices;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.SearchRequestDTO;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.utill.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserSearchService {

    private final RestHighLevelClient client;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public UserSearchService(RestHighLevelClient client) {
        this.client = client;
    }

    public List<User> userGenericSearch(final SearchRequestDTO searchRequestDTO, final Date fromDate, final  Date toDate) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.USER_INDEX,
                searchRequestDTO,
                "createdDate",
                fromDate,
                toDate);
        return searchInternal(request);
    }

    private List<User> searchInternal(final SearchRequest request) {
        if (request == null) {
            log.error("failed to build search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            final SearchHit[] searchHits = response.getHits().getHits();
            final List<User> users = new ArrayList<>(searchHits.length);
            for (SearchHit searchHit : searchHits) {
                String source = searchHit.getSourceAsString();
                users.add(
                        MAPPER.readValue(source, User.class)
                );
            }
            log.info("users found: " + users);
            return users;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
