package com.probal.RabbitMQconsumerElasticSearch.documents.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.probal.RabbitMQconsumerElasticSearch.documents.helper.Indices;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Document(indexName = Indices.USER_INDEX)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword, name = "username")
    private String username;

    @Field(type = FieldType.Keyword, name = "email")
    private String email;

    @Field(type = FieldType.Text, name = "number")
    private String number;

    @Field(type = FieldType.Date, name = "createdDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @Field(type = FieldType.Nested)
    private List<Education> educations;

}
