package com.probal.RabbitMQconsumerElasticSearch.documents.search.utill;

import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.SearchRequestDTO;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.UserSearchRequestDTO;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
public class SearchUtil {

    // For generic(bool) queries
    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final UserSearchRequestDTO searchRequestDTO) {

        if (searchRequestDTO.getToDate() == null && searchRequestDTO.getFromDate() == null) {

            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(getBoolQueryBuilder(searchRequestDTO));

            SearchRequest request = new SearchRequest(indexName);

            request.source(builder);
            return request;
        } else if (searchRequestDTO.getEmail() == null && searchRequestDTO.getUsername() == null && searchRequestDTO.getPhone() == null ) {
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
            request.source(builder);
            return request;
        }
        /*if (fromDate == null || toDate == null) {

            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(getQueryBuilder(searchRequestDTO));

            if (searchRequestDTO.getSortBy() != null) {
                builder = builder.sort(
                        searchRequestDTO.getSortBy(),
                        searchRequestDTO.getSortOrder() != null ? searchRequestDTO.getSortOrder() : SortOrder.ASC
                );
            }

            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;

        } else {
            final QueryBuilder searchQueryBuilder = getQueryBuilder(searchRequestDTO);

            final QueryBuilder rangeQueryBuilder = getQueryBuilder(field, fromDate, toDate);

            final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(searchQueryBuilder).must(rangeQueryBuilder);
            SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(boolQueryBuilder);

            if (searchRequestDTO.getSortBy() != null) {
                builder = builder.sort(
                        searchRequestDTO.getSortBy(),
                        searchRequestDTO.getSortOrder() != null ? searchRequestDTO.getSortOrder() : SortOrder.ASC
                );
            }

            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;
        }*/
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

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.should(usernameMatch).should(emailMatch).should(phoneMatch);
        return queryBuilder;
    }

//    public static QueryBuilder getQueryBuilder(final SearchRequestDTO searchRequestDTO) {
//        if (searchRequestDTO == null) {
//            return null;
//        }
//
//        final List<String> fields = searchRequestDTO.getFields();
//        if (CollectionUtils.isEmpty(fields)) {
//            return null;
//        }
//
//        if (fields.size() > 1) {
//            MultiMatchQueryBuilder queryBuilder = QueryBuilders
//                    .multiMatchQuery(searchRequestDTO.getSearchTerm())
//                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
//                    .operator(Operator.AND);
//            fields.forEach(queryBuilder::field);
//            return queryBuilder;
//        }
//        return fields.stream()
//                .findFirst()
//                .map(field ->
//                        QueryBuilders
//                                .matchQuery(field, searchRequestDTO.getSearchTerm())
//                                .operator(Operator.AND)
//                ).orElse(null);
//    }

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
