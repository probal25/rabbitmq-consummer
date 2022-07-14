package com.probal.RabbitMQconsumerElasticSearch.documents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.UserSearchRequestDTO;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.response.EducationResponse;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.service.UserSearchService;
import com.probal.RabbitMQconsumerElasticSearch.documents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<User> userGenericSearch(@RequestBody final UserSearchRequestDTO requestDTO) {
        return searchService.userGenericSearch(requestDTO);
    }

    @GetMapping("/save")
    public ResponseEntity<?> insertValuesToES() {
        service.generateData();
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/clear")
    public ResponseEntity<?> clearValuesOfES() {
        service.clearData();
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/keywords")
    public ResponseEntity<?> getKeywords(@RequestParam("query") String param) throws JsonProcessingException {
        List<EducationResponse> educationResponses = searchService.getKeywords(param);
        return ResponseEntity.ok(educationResponses);
    }

    @PostMapping("/search_new")
    public List<User> userGenericSearchNew(@RequestBody final UserSearchRequestDTO requestDTO) throws JsonProcessingException {

        return searchService.userGenericSearchNew(requestDTO);

    }

}
