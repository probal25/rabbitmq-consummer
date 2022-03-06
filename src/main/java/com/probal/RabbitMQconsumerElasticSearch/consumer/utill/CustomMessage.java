package com.probal.RabbitMQconsumerElasticSearch.consumer.utill;

import com.probal.RabbitMQconsumerElasticSearch.consumer.dto.UserDto;

import java.util.Date;


public class CustomMessage {

    private String messageId;
    private UserDto messageBody;
    private Date messageDate;

    public CustomMessage(String messageId, UserDto messageBody, Date messageDate) {
        this.messageId = messageId;
        this.messageBody = messageBody;
        this.messageDate = messageDate;
    }

    public CustomMessage() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public UserDto getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(UserDto messageBody) {
        this.messageBody = messageBody;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
