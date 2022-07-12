package com.probal.RabbitMQconsumerElasticSearch.documents.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Education {

    @Field(type = FieldType.Keyword)
    private String name;

    private String score;

    @Field(type = FieldType.Nested)
    private Config config;


}
