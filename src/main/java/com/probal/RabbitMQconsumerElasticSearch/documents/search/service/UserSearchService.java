package com.probal.RabbitMQconsumerElasticSearch.documents.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.documents.helper.Indices;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.UserSearchRequestDTO;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.utill.SearchUtil;
import com.probal.RabbitMQconsumerElasticSearch.documents.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.probal.RabbitMQconsumerElasticSearch.documents.helper.Indices.USER_INDEX;

@Service
@Slf4j
public class UserSearchService {

    private final RestHighLevelClient client;
    private final UserService userService;
    private final ElasticsearchOperations elasticsearchOperations;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public UserSearchService(RestHighLevelClient client, UserService userService, ElasticsearchOperations elasticsearchOperations) {
        this.client = client;
        this.userService = userService;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<User> userGenericSearchNew(final UserSearchRequestDTO userSearchRequestDTO) throws JsonProcessingException {
        NativeSearchQuery nativeSearchQuery = SearchUtil.getNativeSearchQuery(userSearchRequestDTO);
        return this.searchInternalNew(nativeSearchQuery);
    }

    private List<User> searchInternalNew(NativeSearchQuery searchQuery) throws JsonProcessingException {

        SearchHits<User> hits = elasticsearchOperations.search(searchQuery, User.class, IndexCoordinates.of(USER_INDEX));
        final List<User> users = new ArrayList<>();
        hits.getSearchHits().forEach(userSearchHit -> {
            users.add(userSearchHit.getContent());
        });

        return users;
    }

    public List<User> userGenericSearch(final UserSearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO.isEmpty()) {
            return userService.getAllUserFromUserIndex();
        }
        final SearchRequest request = SearchUtil.buildSearchRequest(
                USER_INDEX,
                searchRequestDTO);
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
