package com.LifeTales.domain.feed.controller;

import com.LifeTales.common.User.FeedChecker;
import com.LifeTales.domain.feed.repository.DTO.FeedDataDTO;
import com.LifeTales.domain.feed.repository.DTO.FeedDetailDTO;
import com.LifeTales.domain.feed.repository.DTO.FeedUploadDTO;
import com.LifeTales.domain.feed.service.FeedService;
import com.LifeTales.global.Validator.FeedUploadValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/feed")
public class FeedController {
    private final ObjectMapper objectMapper;
    private final FeedService feedService;
    private final FeedUploadValidator feedUploadValidator;

    private final FeedChecker feedChecker;
    public FeedController(ObjectMapper objectMapper, FeedService feedService, FeedUploadValidator feedUploadValidator, FeedChecker feedChecker) {
        this.objectMapper = objectMapper;
        this.feedService = feedService;
        this.feedUploadValidator = feedUploadValidator;
        this.feedChecker = feedChecker;
    }

    @PostMapping("/upload/detail/")
    public ResponseEntity FeedUpload( @RequestParam("userSeq") Long userSeq,
                                           @RequestParam("familySeq") Long familySeq,
                                           @RequestParam(value = "uploadIMG", required = false) List<MultipartFile> uploadIMGs,
                                           @RequestParam("content") String content) throws IOException {

        log.info("FeedUpload-Start >> userSeq : {} familySeq : {}", userSeq , familySeq);

        FeedUploadDTO uploadData = new FeedUploadDTO();
        uploadData.setUserSeq(userSeq);
        uploadData.setFamilySeq(familySeq);
        uploadData.setContent(content);

        if (uploadIMGs != null && !uploadIMGs.isEmpty()) { //uploadImage가 있을 경우
            List<byte[]> imageBytesList = new ArrayList<>();

            for (MultipartFile uploadIMG : uploadIMGs) {
                try {
                    byte[] imageBytes = uploadIMG.getBytes();
                    imageBytesList.add(imageBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 처리 중 오류가 발생할 경우 예외 처리
                }
            }

            uploadData.setUploadImages(imageBytesList);
        } else {
            uploadData.setUploadImages(null);
        }


        log.info("FeedUpload data Check - Stat");
        //Validation Start
        String returnValidText = feedUploadValidator.feedUploadValidate(uploadData);
        //Validation End
        log.info("FeedUpload data Check - End");
        ResponseEntity<String> responseEntity;
        if ("Success".equals(returnValidText)) {
            boolean userIdCheck = feedChecker.doesUserSeqExist(userSeq);
            boolean familyIdCheck = feedChecker.doesFamilySeqExist(familySeq);
            if (userIdCheck && familyIdCheck){
                String return_text = feedService.Feed_upload_service(uploadData);
                if ("Success".equals(return_text)) {
                    log.info("FamilySignUp service Success , {}", uploadData.getUserSeq());
                    // 회원가입 성공한 경우 처리
                    responseEntity = ResponseEntity.ok("upload Success");
                } else if ("DataAccessException".equals(return_text)) {
                    log.info("FeedUpload service DataAccessException , {}", uploadData.getUserSeq());
                    // 데이터베이스 예외 발생한 경우 처리
                    responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataAccessException");
                } else if ("RuntimeException".equals(return_text)) {
                    log.info("FeedUpload service RuntimeException , {}", uploadData.getUserSeq());
                    // 런타임 예외 발생한 경우 처리
                    responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RuntimeException");
                } else {
                    log.info("FeedUpload service don't Know Error , Please contact about Developer, {}", uploadData.getUserSeq());
                    // 기타 예외나 다른 경우 처리
                    responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("don't Know Error");
                }
                return  responseEntity;
            } else {
                if (!userIdCheck){
                    log.info("유저가 존재하지 않습니다");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저가 존재하지 않습니다");
                }else{
                    log.info("가족이 존재하지 않습니다");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("가족이 존재하지 않습니다");
                }

            }


        }else{
            log.info("FeedUpload validation failed: {}", returnValidText);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnValidText);
        }

    }

    @GetMapping("/feedDataUser/{id}")
    public ResponseEntity GetFeedDataForUser(@PathVariable(required = true) String id,
                                                          @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
                                                          Pageable pageable) throws IOException {
        log.info("lifeTalesFeedDataForUserGetTest >> id : {}", id);
        Page<FeedDataDTO> feedDataDTO = feedService.getFeedDataForUser(id, pageNum , pageable);
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

    @GetMapping("/feedDataFamily/{nickname}")
    public ResponseEntity<Page<FeedDataDTO>> getFeedDataForFamily(
            @PathVariable(required = true) String nickname,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
            Pageable pageable
    ) throws IOException {
        log.info("lifeTalesFeedDataForFamilyGetTest >> id : {}", nickname);
        Page<FeedDataDTO> feedPage = feedService.getFeedDataForFamily(nickname, pageNum, pageable);

        if (feedPage == null) {
            log.info("null >> ");
            return null;
        } else {
            String json = objectMapper.writeValueAsString(feedPage);
            log.info(json);
            log.info("sucess");
            return ResponseEntity.ok(feedPage);
        }


    }
    @GetMapping("/feedDetail/{seq}")
    public ResponseEntity GetFeedDetail(@PathVariable Long seq) throws IOException {
        log.info("lifeTalesFeedDataDetailGet>> seq : {}", seq);
        FeedDetailDTO feedDetailDTO = feedService.getFeedDetail(seq);
        if (feedDetailDTO == null) {
            log.info("null >> ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
        } else {
            String json = objectMapper.writeValueAsString(feedDetailDTO);
            log.info(json);
            log.info("sucess");
            return ResponseEntity.ok(json);
        }
    }
}
