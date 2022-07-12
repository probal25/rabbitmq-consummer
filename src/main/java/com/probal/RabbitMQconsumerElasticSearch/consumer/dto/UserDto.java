package com.probal.RabbitMQconsumerElasticSearch.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String username;
    private String userPassword;
    private String userEmail;
    private String userNumber;
    private Date createdDate;
}
