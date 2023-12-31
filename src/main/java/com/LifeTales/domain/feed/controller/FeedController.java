package com.LifeTales.domain.feed.controller;

import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.feed.repository.DAO.FeedDataDAO;
import com.LifeTales.domain.feed.repository.DAO.FeedDetailDAO;
import com.LifeTales.domain.feed.repository.DTO.FeedUploadDTO;
import com.LifeTales.domain.feed.service.FeedService;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.global.Validator.FeedUploadValidator;
import com.LifeTales.global.util.UseTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/feed")
@CrossOrigin(origins = {"http://192.168.35.174:3000", "http://3.39.251.34:3000"})
public class FeedController {
    private final ObjectMapper objectMapper;
    private final FeedService feedService;
    private final FeedUploadValidator feedUploadValidator;

    private final UserRepository userRepository;
    private final UserIdChecker userIdChecker;
    private final UseTokenUtil tokenUtil;
    public FeedController(ObjectMapper objectMapper, FeedService feedService, FeedUploadValidator feedUploadValidator, UserRepository userRepository, UserIdChecker userIdChecker, UseTokenUtil tokenUtil) {
        this.objectMapper = objectMapper;
        this.feedService = feedService;
        this.feedUploadValidator = feedUploadValidator;
        this.userRepository = userRepository;
        this.userIdChecker = userIdChecker;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping("/upload/detail/")
    public ResponseEntity FeedUpload(HttpServletRequest request ,
                                           @RequestParam(value = "uploadIMG", required = false) List<MultipartFile> uploadIMGs,
                                           @RequestParam("content") String content) throws IOException {

        String id = tokenUtil.findUserIdForJWT(request);
        if(userIdChecker.doesIdExist(id)){
            User user = userRepository.findById(id);
            if(user.getFamilySeq() != null){
                Family family = user.getFamilySeq();
                log.info("FeedUpload-Start >> userSeq : {} familySeq : {}", user.getSeq() , family.getSeq());
                FeedUploadDTO uploadData = new FeedUploadDTO();
                uploadData.setUserSeq(user.getSeq());
                uploadData.setFamilySeq(family.getSeq());
                uploadData.setContent(content);
                if (uploadIMGs != null && !uploadIMGs.isEmpty()) { //uploadImage가 있을 경우
                    List<byte[]> imageBytesList = new ArrayList<>();

                    for (MultipartFile uploadIMG : uploadIMGs) {
                        try {
                            byte[] imageBytes = uploadIMG.getBytes();
                            imageBytesList.add(imageBytes);
                            log.info("FeedUploadImageCheck - True");
                        } catch (IOException e) {
                            e.printStackTrace();
                            // 처리 중 오류가 발생할 경우 예외 처리
                        }
                    }

                    uploadData.setUploadImages(imageBytesList);
                } else {
                    uploadData.setUploadImages(null);
                    log.info("FeedUploadImageCheck - False");
                }
                log.info("FeedUpload data Check - Stat");
                //Validation Start
                String returnValidText = feedUploadValidator.feedUploadValidate(uploadData);
                //Validation End
                log.info("FeedUpload data Check - End");
                ResponseEntity<String> responseEntity;
                if ("Success".equals(returnValidText)) {
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
                }else{
                    log.info("FeedUpload validation failed: {}", returnValidText);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnValidText);
                }

            }else{
                log.info("존재하지 않는 Family입니다");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("가족이 존재하지 않습니다");
            }
        }else{
            log.info("존재하지 않는 User입니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저가 존재하지 않습니다");
        }

    }

    @GetMapping("/feedDataUser/")
    public ResponseEntity GetFeedDataForUser(HttpServletRequest request,
                                                          @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
                                                          Pageable pageable) throws IOException {
        String id = tokenUtil.findUserIdForJWT(request);
        if(userIdChecker.doesIdExist(id)){
            log.info("lifeTalesFeedDataForUserGetTest >> id : {}", id);
            Page<FeedDataDAO> feedDataDTO = feedService.getFeedDataForUser(id, pageNum , pageable);
            if (feedDataDTO == null) {
                log.info("null >> ");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("feedData 가져오기 실패");
            } else {
                String json = objectMapper.writeValueAsString(feedDataDTO);
                log.info(json);
                log.info("sucess");
                return ResponseEntity.ok(json);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
    }

    @GetMapping("/feedDataFamily/")
    public ResponseEntity getFeedDataForFamily(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
            Pageable pageable
    ) throws IOException {
        String id = tokenUtil.findUserIdForJWT(request);
        if(userIdChecker.doesIdExist(id)){
            User user = userRepository.findById(id);
            if(user.getFamilySeq()!=null){
                Family family = user.getFamilySeq();
                log.info("lifeTalesFeedDataForFamilyGetTest >> id : {}", family.getNickname());
                Page<FeedDataDAO> feedPage = feedService.getFeedDataForFamily(family.getNickname(), pageNum, pageable);
                if (feedPage == null) {
                    log.info("null >> ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("feed data 가져오기 실패");
                } else {
                    String json = objectMapper.writeValueAsString(feedPage);
                    log.info(json);
                    log.info("sucess");
                    return ResponseEntity.ok(feedPage);
                }
            }else{
                log.info("not exists family");
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("family가 존재하지 않음");
            }
        }else{
            log.info("not exists user");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user가 존재하지 않음");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IOException");
    }
    @GetMapping("/feedDetail/{seq}")
    public ResponseEntity GetFeedDetail(@PathVariable Long seq) throws IOException {
        log.info("lifeTalesFeedDataDetailGet>> seq : {}", seq);
        FeedDetailDAO feedDetailDTO = feedService.getFeedDetail(seq);
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
