package com.probal.RabbitMQconsumerElasticSearch.documents.dto;

import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserDto implements Serializable {

    private String name;

    private String email;

    private String number;

    public static UserDto from(User user) {
        return UserDto.builder()
                .name(user.getUsername())
                .email(user.getEmail())
                .number(user.getNumber()).build();
    }

}
