package com.probal.RabbitMQconsumerElasticSearch.documents.dao;

import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserDao extends ElasticsearchRepository<User, String> {

    List<User> findAll();
}
