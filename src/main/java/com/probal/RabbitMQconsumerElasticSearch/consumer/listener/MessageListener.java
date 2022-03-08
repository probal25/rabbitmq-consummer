package com.probal.RabbitMQconsumerElasticSearch.consumer.listener;

import com.probal.RabbitMQconsumerElasticSearch.documents.service.UserService;
import com.probal.RabbitMQconsumerElasticSearch.consumer.dto.UserDto;
import com.probal.RabbitMQconsumerElasticSearch.consumer.helper.MQConfigProperties;
import com.probal.RabbitMQconsumerElasticSearch.consumer.utill.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

    private final UserService userService;

    @Autowired
    public MessageListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = MQConfigProperties.USER_MESSAGE_QUEUE)
    public void listenFromMessageQueue(CustomMessage customMessage) {

        UserDto userDto = customMessage.getMessageBody();
        this.saveUserToElasticSearch(userDto);

    }

    private void saveUserToElasticSearch(UserDto userDto) {
        log.info("=========>Sending data to Elastic Search Index<===========");
        userService.saveUserToUserIndex(userDto);
    }
}
