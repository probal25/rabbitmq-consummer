package com.probal.RabbitMQconsumerElasticSearch.documents.search.payload;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Date;
import java.util.Objects;

@Data
public class UserSearchRequestDTO {
    private String username;
    private String email;
    private String phone;
    private Date fromDate;
    private Date toDate;
    private String eduName;

    public boolean isEmpty() {
        return (
                this.username == null
                && this.email == null
                && this.phone == null
                && this.fromDate == null
                && this.toDate == null
                && this.eduName == null
        );
    }
}
