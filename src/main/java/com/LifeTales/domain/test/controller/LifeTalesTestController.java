package com.LifeTales.domain.test.controller;

import com.LifeTales.domain.family.repository.DTO.FamilyDataDTO;
import com.LifeTales.domain.family.service.FamilyService;
import com.LifeTales.domain.feed.repository.DTO.FeedDataDTO;
import com.LifeTales.domain.feed.service.FeedService;
import com.LifeTales.domain.test.domain.Test;
import com.LifeTales.domain.test.repository.dto.TestDTO;
import com.LifeTales.domain.test.service.LifeTalesTestService;
import com.LifeTales.domain.user.repository.DTO.UserDataDTO;
import com.LifeTales.domain.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class LifeTalesTestController {
    private final ObjectMapper objectMapper;

    private final LifeTalesTestService lifeTalesTestService;

    private final UserService userService;

    private final FeedService feedService;
    private final FamilyService familyService;
    public LifeTalesTestController(ObjectMapper objectMapper, LifeTalesTestService lifeTalesTestService, UserService userService, FamilyService familyService, FeedService feedService) {
        this.objectMapper = objectMapper;
        this.lifeTalesTestService = lifeTalesTestService;
        this.userService = userService;
        this.familyService = familyService;
        this.feedService = feedService;
    }
    @ResponseBody
    @GetMapping("/test/get/{name}")
    public ResponseEntity lifeTalesGetTest(@PathVariable String name) throws JsonProcessingException {
        log.info("lifeTalesGetTest -> name : {}", name);
        String json = objectMapper.writeValueAsString(name);
        log.info(json);
        return ResponseEntity.ok(json);
    }
    @ResponseBody
    @PostMapping("/test/post/")
    public ResponseEntity lifeTalesPostTest(@RequestBody TestDTO testDTO) throws JsonProcessingException {
        log.info("lifeTalesPostTest-> testDTO  -> Name : {} , age : {}  , NickName : {}", testDTO.getName(),testDTO.getAge(),testDTO.getNickName());
        String json = objectMapper.writeValueAsString(testDTO);
        log.info(json);
        return ResponseEntity.ok(json);
    }

    @ResponseBody
    @PostMapping("/test/create/")
    public ResponseEntity lifeTalesCreateTest(@RequestBody TestDTO testDTO) throws JsonProcessingException {
        log.info("lifeTalesPostTest-> testDTO  -> Name : {} , age : {}  , NickName : {}", testDTO.getName(),testDTO.getAge(),testDTO.getNickName());
        String returnText = lifeTalesTestService.dbtest_create_service(testDTO);
        String json = objectMapper.writeValueAsString(returnText);
        log.info(json);
        return ResponseEntity.ok(json);
    }
    @ResponseBody
    @GetMapping("/test/read/{name}")
    public ResponseEntity lifeTalesReadTest(@PathVariable String name) throws JsonProcessingException {
        log.info("lifeTalesReadTest -> name : {}", name);
        Test testData = lifeTalesTestService.dbtest_read_service(name);
        String json = objectMapper.writeValueAsString(testData);
        log.info(json);
        return ResponseEntity.ok(json);
    }
    @ResponseBody
    @GetMapping("/test/userData/{userId}")
    public ResponseEntity lifeTalesUserDataGetTest(@PathVariable String userId) throws IOException {
        log.info("lifeTalesUserDataGetTest >> id : {}" , userId);
        UserDataDTO userDataDTO = userService.getDataForUser(userId);
        if(userDataDTO == null){
            log.info("null >> ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
        }else{
            String json = objectMapper.writeValueAsString(userDataDTO);
            log.info(json);
            return ResponseEntity.ok(json);
        }

    }

    @ResponseBody
    @GetMapping("/test/familyData/{nickname}")
    public ResponseEntity lifeTalesFamilyDataGetTest(@PathVariable String nickname) throws IOException {
        log.info("lifeTalesFamilyDataGetTest >> id : {}" , nickname);
        FamilyDataDTO familyDataDTO = familyService.getDataForFamily(nickname);
        if(familyDataDTO == null){
            log.info("null >> ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
        }else{
            String json = objectMapper.writeValueAsString(familyDataDTO);
            log.info(json);
            return ResponseEntity.ok(json);
        }

    }
    @ResponseBody
    @GetMapping("/test/feedDataFamily/{nickname}")
    public ResponseEntity lifeTalesFeedDataForFamilyGetTest(@PathVariable String nickname) throws IOException {
        log.info("lifeTalesFeedDataForFamilyGetTest >> id : {}", nickname);
        List<FeedDataDTO> feedDataDTO = feedService.getFeedDataForFamily(nickname);
        if (feedDataDTO == null) {
            log.info("null >> ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
        } else {
            String json = objectMapper.writeValueAsString(feedDataDTO);
            log.info(json);
            log.info("sucess");
            return ResponseEntity.ok(json);
        }


    }

    @ResponseBody
    @GetMapping("/test/feedDataUser/{id}")
    public ResponseEntity lifeTalesFeedDataForUserGetTest(@PathVariable String id) throws IOException {
        log.info("lifeTalesFeedDataForUserGetTest >> id : {}", id);
        List<FeedDataDTO> feedDataDTO = feedService.getFeedDataForUser(id);
        if (feedDataDTO == null) {
            log.info("null >> ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
        } else {
            String json = objectMapper.writeValueAsString(feedDataDTO);
            log.info(json);
            log.info("sucess");
            return ResponseEntity.ok(json);
        }


    }
}
