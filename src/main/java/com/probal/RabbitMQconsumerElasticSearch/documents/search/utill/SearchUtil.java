package com.probal.RabbitMQconsumerElasticSearch.documents.search.utill;

import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.UserSearchRequestDTO;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
public class SearchUtil {

    // new implementation for search

    public static NativeSearchQuery getNativeSearchQuery(final UserSearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO.getToDate() == null && searchRequestDTO.getFromDate() == null) {

            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(getBoolQueryBuilder(searchRequestDTO));
            return nativeSearchQueryBuilder.build();

        } else if (searchRequestDTO.getEmail() == null && searchRequestDTO.getUsername() == null && searchRequestDTO.getPhone() == null) {

            final QueryBuilder rangeQueryBuilder = getQueryBuilder("createdDate", searchRequestDTO.getFromDate(), searchRequestDTO.getToDate());

            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(rangeQueryBuilder);
            return nativeSearchQueryBuilder.build();

        } else {

            final QueryBuilder searchQueryBuilder = getBoolQueryBuilder(searchRequestDTO);
            final QueryBuilder rangeQueryBuilder = getQueryBuilder("createdDate", searchRequestDTO.getFromDate(), searchRequestDTO.getToDate());
            final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(searchQueryBuilder).must(rangeQueryBuilder);

            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
            return nativeSearchQueryBuilder.build();
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

            final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(searchQueryBuilder).must(rangeQueryBuilder);
            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(boolQueryBuilder);

            SearchRequest request = new SearchRequest(indexName);
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
            NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();

            request.source(builder);
            return request;
        }

    }

    private static BoolQueryBuilder getBoolQueryBuilder(List<MatchQueryBuilder> matchQueryBuilders) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for (MatchQueryBuilder matchQueryBuilder : matchQueryBuilders) {
            if (matchQueryBuilder != null) {
                queryBuilder = queryBuilder.should(matchQueryBuilder);
            }
        }
        return queryBuilder;
    }

    private static BoolQueryBuilder getBoolQueryBuilder(UserSearchRequestDTO searchRequestDTO) {
        MatchQueryBuilder usernameMatch = null;
        MatchQueryBuilder emailMatch = null;
        MatchQueryBuilder phoneMatch = null;
        if (searchRequestDTO.getUsername() != null) {
            usernameMatch = QueryBuilders.matchQuery("username", searchRequestDTO.getUsername());
        }
        if (searchRequestDTO.getEmail() != null) {
            emailMatch = QueryBuilders.matchQuery("email", searchRequestDTO.getEmail());
        }
        if (searchRequestDTO.getPhone() != null) {
            phoneMatch = QueryBuilders.matchQuery("phone", searchRequestDTO.getPhone());
        }

        List<MatchQueryBuilder> matchQueryBuilders = new ArrayList<>();
        matchQueryBuilders.add(usernameMatch);
        matchQueryBuilders.add(emailMatch);
        matchQueryBuilders.add(phoneMatch);


        System.out.println("");
        return getBoolQueryBuilder(matchQueryBuilders);
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
}
