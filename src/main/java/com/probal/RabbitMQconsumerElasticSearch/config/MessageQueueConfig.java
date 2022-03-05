package com.probal.RabbitMQconsumerElasticSearch.config;

import com.probal.RabbitMQconsumerElasticSearch.helper.MQConfigProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageQueueConfig {

    @Bean
    public Queue queue() {
        return new Queue(MQConfigProperties.USER_MESSAGE_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(MQConfigProperties.USER_MESSAGE_EXCHANGE_TOPIC);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(MQConfigProperties.USER_MESSAGE_ROUTING_KEY);

    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}