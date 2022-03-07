package com.probal.RabbitMQconsumerElasticSearch.documents.controller;

import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.SearchRequestDTO;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.service.UserSearchService;
import com.probal.RabbitMQconsumerElasticSearch.documents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/search")
    public List<User> userGenericSearch(@RequestBody final SearchRequestDTO requestDTO) {

        searchService.userGenericSearch(requestDTO)
    }
}
