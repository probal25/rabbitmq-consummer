package com.probal.RabbitMQconsumerElasticSearch.documents.service;

import com.probal.RabbitMQconsumerElasticSearch.documents.dao.UserDao;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.Config;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.Education;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.consumer.dto.UserDto;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.response.EducationResponse;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.utill.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void saveUserToUserIndex(final UserDto userDto, List<Education> educations) {
        try {
            User user = new User();
            user.setId(userDto.getUserId().toString());
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getUserEmail());
            user.setNumber(userDto.getUserNumber());
            user.setCreatedDate(userDto.getCreatedDate());


            user.setEducations(educations);


            userDao.save(user);
            log.info(userDto + " -> is saved to User Index");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<User> getAllUserFromUserIndex() {
        return userDao.findAll();
    }

    public void clearData() {
        userDao.deleteAll();
    }

    public void generateData() {
        saveUserToUserIndex(UserDto.builder().userId(1L).userEmail("dasdasdas@dsadas").username("probal").userNumber("01988509770").createdDate(new Date()).build(),
                List.of(
                        generateEducation("HSC", "5.00", "Ha Ha"),
                        generateEducation("JSC", "5.00", "Ha Ha"),
                        generateEducation("PSC", "5.00", "Ha Ha"),
                        generateEducation("SSC", "5.00", "Ha Ha")
                ));
        saveUserToUserIndex(UserDto.builder().userId(2L).userEmail("kluhu@dsadas").username("rihan").userNumber("01988509771").createdDate(new Date()).build(),
                List.of(
                        generateEducation("HSC", "4.00", "Ha Ha")
                ));
        saveUserToUserIndex(UserDto.builder().userId(3L).userEmail("bcvbcnmcg@dsadas").username("ammu").userNumber("01988509772").createdDate(new Date()).build(),
                List.of(
                        generateEducation("JSC", "5.00", "Ha Ha"),
                        generateEducation("PSC", "5.00", "Ha Ha")
                ));
        saveUserToUserIndex(UserDto.builder().userId(4L).userEmail("dasdbvbvbasdas@dsadas").username("abbu").userNumber("01988509773").createdDate(new Date()).build(),
                List.of(
                        generateEducation("HSC", "5.00", "Ha Ha"),
                        generateEducation("SSC", "5.00", "Ha Ha")
                ));
        saveUserToUserIndex(UserDto.builder().userId(5L).userEmail("dasdfas@dsadas").username("gulu").userNumber("01988509774").createdDate(new Date()).build(),
                List.of(
                        generateEducation("SSC", "5.00", "Ha Ha")
                ));
    }

    private Education generateEducation(String name, String score, String configName) {
        return new Education(name, score, new Config(true, configName));
    }

}
