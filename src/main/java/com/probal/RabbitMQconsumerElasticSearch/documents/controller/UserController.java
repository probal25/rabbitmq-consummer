package com.probal.RabbitMQconsumerElasticSearch.documents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.SearchRequestDTO;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.UserSearchRequestDTO;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.service.UserSearchService;
import com.probal.RabbitMQconsumerElasticSearch.documents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class UserController {

    private final UserService service;
    private final UserSearchService searchService;

    @Autowired
    public UserController(UserService service, UserSearchService searchService) {
        this.service = service;
        this.searchService = searchService;
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return service.getAllUserFromUserIndex();
    }

    @PostMapping("/search_new")
    public List<User> userGenericSearchNew(@RequestBody final UserSearchRequestDTO requestDTO) throws JsonProcessingException {

        return searchService.userGenericSearchNew(requestDTO);

    }

}
