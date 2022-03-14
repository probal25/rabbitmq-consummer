package com.probal.RabbitMQconsumerElasticSearch.documents.search.utill;

import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.UserSearchRequestDTO;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Date;

@NoArgsConstructor
public class SearchUtil {

    public static final String RANGE_QUERY_FIELD = "createdDate";

    // new implementation for search

    public static NativeSearchQuery getNativeSearchQuery(final UserSearchRequestDTO searchRequestDTO) {

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        return nativeSearchQueryBuilder.withQuery(getBoolQueryBuilder(searchRequestDTO)).build();

    }

    private static BoolQueryBuilder getBoolQueryBuilder(UserSearchRequestDTO searchRequestDTO) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(searchRequestDTO.getUsername())) {
            boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery("username", searchRequestDTO.getUsername()));
        }
        if (!StringUtils.isEmpty(searchRequestDTO.getEmail())) {
            boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery("email", searchRequestDTO.getEmail()));
        }
        if (!StringUtils.isEmpty(searchRequestDTO.getPhone())) {
            boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery("phone", searchRequestDTO.getPhone()));
        }
        if (searchRequestDTO.getFromDate() != null) {
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.rangeQuery(RANGE_QUERY_FIELD).gte(searchRequestDTO.getFromDate()));
        }
        if (searchRequestDTO.getToDate() != null) {
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.rangeQuery(RANGE_QUERY_FIELD).lte(searchRequestDTO.getToDate()));
        }

        return boolQueryBuilder;

    }

}
