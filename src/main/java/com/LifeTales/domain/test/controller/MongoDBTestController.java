package com.LifeTales.domain.test.controller;

import com.LifeTales.domain.test.service.MongoDBTestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MongoDBTestController {
    private final MongoDBTestService mongoDBTestService;

    @GetMapping("api/test/testMongo/{test}")
    public String findTestResult_controller(@PathVariable String test) throws JsonProcessingException {
        String returntext = mongoDBTestService.testGetTest(test);
        return returntext;
    }

    @GetMapping("api/test/testMongo/save/{test}")
    public String SaveTestResult_controller(@PathVariable String test) throws JsonProcessingException {
        String returntext = mongoDBTestService.testSaveTest(test , 1);
        return returntext;
    }

}
