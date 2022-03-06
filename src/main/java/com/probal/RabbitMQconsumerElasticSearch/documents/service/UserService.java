package com.probal.RabbitMQconsumerElasticSearch.documents.service;

import com.probal.RabbitMQconsumerElasticSearch.documents.dao.UserDao;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.consumer.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void saveUserToUserIndex(final UserDto userDto) {
        try {
            User user = new User();
            user.setId(userDto.getUserId().toString());
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getUserPassword());

            userDao.save(user);
            System.out.println(userDto + " -> is saved to User Index");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<User> getAllUserFromUserIndex() {
        return userDao.findAll();
    }
}
