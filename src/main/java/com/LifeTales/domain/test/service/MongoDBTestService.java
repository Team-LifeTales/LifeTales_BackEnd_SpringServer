package com.LifeTales.domain.test.service;

import com.LifeTales.domain.test.domain.MongoTest;
import com.LifeTales.domain.test.repository.MongoDBTestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MongoDBTestService {

    private  final MongoDBTestRepository mongoDBTestRepository;

    public String testGetTest(String test) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(mongoDBTestRepository.findByTest(test));
    }
    public String testSaveTest(String test , int testInt){
        MongoTest mongoTest =new MongoTest();
        mongoTest.setTest(test);
        mongoTest.setTestInt(testInt);
        if(mongoDBTestRepository.findByTest(test) != null){
            log.info("[Service][update] name is already exist!!");
            return "this test already exists";
        }else{
            mongoDBTestRepository.save(mongoTest);
            return "success";
        }
    }


}
