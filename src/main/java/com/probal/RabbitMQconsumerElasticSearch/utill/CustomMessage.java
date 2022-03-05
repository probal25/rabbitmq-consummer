package com.probal.RabbitMQconsumerElasticSearch.utill;

import com.probal.RabbitMQconsumerElasticSearch.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomMessage implements Serializable {

    private String messageId;
    private UserDto messageBody;
    private Date messageDate;


    //    public CustomMessage(Object messageBody) {
//        this.messageBody = messageBody;
//    }
}
