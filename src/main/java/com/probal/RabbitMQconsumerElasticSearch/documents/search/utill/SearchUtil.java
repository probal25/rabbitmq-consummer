package com.probal.RabbitMQconsumerElasticSearch.documents.search.utill;

import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.UserSearchRequestDTO;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

@NoArgsConstructor
public class SearchUtil {

    // new implementation for search

    public static NativeSearchQuery getNativeSearchQuery(final UserSearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO.getToDate() == null && searchRequestDTO.getFromDate() == null) {

            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            return nativeSearchQueryBuilder.withQuery(getBoolQueryBuilder(searchRequestDTO)).build();

        } else if (StringUtils.isEmpty(searchRequestDTO.getEmail())
                && StringUtils.isEmpty(searchRequestDTO.getUsername())
                && StringUtils.isEmpty(searchRequestDTO.getPhone())) {

            final QueryBuilder rangeQueryBuilder = getQueryBuilder("createdDate", searchRequestDTO.getFromDate(), searchRequestDTO.getToDate());

            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            return nativeSearchQueryBuilder.withQuery(rangeQueryBuilder).build();

        } else {

            final QueryBuilder searchQueryBuilder = getBoolQueryBuilder(searchRequestDTO);
            final QueryBuilder rangeQueryBuilder = getQueryBuilder("createdDate", searchRequestDTO.getFromDate(), searchRequestDTO.getToDate());
            final BoolQueryBuilder boolQueryBuilder = boolQuery().must(searchQueryBuilder).must(rangeQueryBuilder);

            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            return nativeSearchQueryBuilder.withQuery(boolQueryBuilder).build();
        }
    }

    // For generic(bool) queries
    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final UserSearchRequestDTO searchRequestDTO) {

        if (searchRequestDTO.getToDate() == null && searchRequestDTO.getFromDate() == null) {

            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(getBoolQueryBuilder(searchRequestDTO));

            SearchRequest request = new SearchRequest(indexName);

            request.source(builder);
            return request;
        } else if (searchRequestDTO.getEmail() == null && searchRequestDTO.getUsername() == null && searchRequestDTO.getPhone() == null) {
            final QueryBuilder rangeQueryBuilder = getQueryBuilder("createdDate", searchRequestDTO.getFromDate(), searchRequestDTO.getToDate());
            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(rangeQueryBuilder);

            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;
        } else {
            final QueryBuilder searchQueryBuilder = getBoolQueryBuilder(searchRequestDTO);

            final QueryBuilder rangeQueryBuilder = getQueryBuilder("createdDate", searchRequestDTO.getFromDate(), searchRequestDTO.getToDate());

            final BoolQueryBuilder boolQueryBuilder = boolQuery().must(searchQueryBuilder).must(rangeQueryBuilder);
            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(boolQueryBuilder);

            SearchRequest request = new SearchRequest(indexName);

            request.source(builder);
            return request;
        }

    }

    private static BoolQueryBuilder getBoolQueryBuilder(UserSearchRequestDTO searchRequestDTO) {

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        if (!StringUtils.isEmpty(searchRequestDTO.getUsername())) {
            boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery("username", searchRequestDTO.getUsername()));
        }
        if (!StringUtils.isEmpty(searchRequestDTO.getEmail())) {
            boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery("email", searchRequestDTO.getEmail()));
        }
        if (!StringUtils.isEmpty(searchRequestDTO.getPhone())) {
            boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery("phone", searchRequestDTO.getPhone()));
        }
//        boolQueryBuilder = boolQueryBuilder.should(
//                nestedQuery(
//                        "educations",
//                        boolQuery().must(QueryBuilders.matchQuery("educations.config.isVisible", true)),
//                        ScoreMode.Total
//                ));

        boolQueryBuilder = boolQueryBuilder
                .must(nestedQuery("educations.config", termQuery("educations.config.isVisible", true), ScoreMode.Total));
//        Map<String, Boolean> propertyValues = new HashMap<>();
//        propertyValues.put("educations.config.isVisible", true);
//        boolQueryBuilder = boolQueryBuilder.must(nestedBoolQuery(propertyValues, "educations.config"));

        return boolQueryBuilder;

    }

    public static QueryBuilder getQueryBuilder(final String field, final Date fromDate, final Date toDate) {
        if (fromDate != null && toDate == null) {
            return QueryBuilders.rangeQuery(field).gte(fromDate);
        }
        if (fromDate == null && toDate != null) {
            return QueryBuilders.rangeQuery(field).lt(toDate);
        }
        return QueryBuilders.rangeQuery(field).gte(fromDate).lt(toDate);
    }

    public static NestedQueryBuilder nestedBoolQuery(final Map<String, Boolean> propertyValues, final String nestedPath) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<String> iterator = propertyValues.keySet().iterator();

        while (iterator.hasNext()) {
            String propertyName = iterator.next();
            Boolean propertyValue = propertyValues.get(propertyName);
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery(propertyName, propertyValue);
            boolQueryBuilder.must(matchQuery);
        }

        return QueryBuilders.nestedQuery(nestedPath, boolQueryBuilder, ScoreMode.Total);
    }
}
