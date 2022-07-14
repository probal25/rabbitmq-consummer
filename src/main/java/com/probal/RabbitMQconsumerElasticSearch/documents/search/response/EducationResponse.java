package com.probal.RabbitMQconsumerElasticSearch.documents.search.response;


import com.probal.RabbitMQconsumerElasticSearch.documents.document.Education;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationResponse implements Serializable {

    private String name;

    public static EducationResponse from(Education education) {
        return EducationResponse.builder().name(education.getName()).build();
    }
}
