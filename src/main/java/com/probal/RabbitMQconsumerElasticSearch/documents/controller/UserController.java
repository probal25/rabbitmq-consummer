/*
*

token : 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

# command for elastic search [http://localhost:9200/]

docker run -p 9200:9200 \
  -e "discovery.type=single-node" \
  docker.elastic.co/elasticsearch/elasticsearch:7.10.0

# command for rabbit mq [http://localhost:15672]

docker run -d -p 5672:5672 -p 15672:15672 --name my-rabbit rabbitmq:3-management



* */

package com.probal.RabbitMQconsumerElasticSearch.documents.controller;

import com.probal.RabbitMQconsumerElasticSearch.documents.document.User;
import com.probal.RabbitMQconsumerElasticSearch.documents.search.payload.SearchRequestDTO;
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

    @PostMapping("/search")
    public List<User> userGenericSearch(@RequestBody final SearchRequestDTO requestDTO,
                                        @RequestParam(value = "from_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                        @RequestParam(value = "to_date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

        return searchService.userGenericSearch(requestDTO, fromDate, toDate);

    }
}
