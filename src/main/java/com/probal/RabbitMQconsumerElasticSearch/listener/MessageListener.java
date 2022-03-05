package com.probal.RabbitMQconsumerElasticSearch.listener;

import com.probal.RabbitMQconsumerElasticSearch.helper.MQConfigProperties;
import com.probal.RabbitMQconsumerElasticSearch.utill.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener {

    @RabbitListener(queues = MQConfigProperties.USER_MESSAGE_QUEUE)
    public void listenFromMessageQueue(CustomMessage customMessage) {

        System.out.println(customMessage);

    }
}
