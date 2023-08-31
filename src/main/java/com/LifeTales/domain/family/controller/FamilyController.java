package com.LifeTales.domain.family.controller;

import com.LifeTales.common.User.FamilyNicknameChecker;
import com.LifeTales.domain.family.repository.DTO.FamilySignUpDTO;
import com.LifeTales.domain.family.service.FamilyService;
import com.LifeTales.global.Validator.FamilySignUpValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/family")
@CrossOrigin(origins = {"http://172.20.144.1:3000", "http://3.39.251.34:3000"})
public class FamilyController {
    private final ObjectMapper objectMapper;
    private final FamilyService familyService;
    private final FamilySignUpValidator familyValidator;

    private final FamilyNicknameChecker familyNicknameChecker;
    public FamilyController(ObjectMapper objectMapper, FamilyService familyService, FamilySignUpValidator familyValidator, FamilyNicknameChecker familyNicknameChecker) {
        this.objectMapper = objectMapper;
        this.familyService = familyService;
        this.familyValidator = familyValidator;
        this.familyNicknameChecker = familyNicknameChecker;
    }

    @PostMapping("/createFamily/detail")
    public ResponseEntity FamilySignUp(@RequestParam("nickname") String nickname,
                                            @RequestParam(value = "profileIMG", required = false) MultipartFile profileIMG,
                                            @RequestParam("introduce") String introduce,
                                            @RequestParam("userSeq") Long userSeq) throws IOException {
        log.info("basicFamilySignUp-Start >> nickName : {}" , nickname);

        FamilySignUpDTO signUpData = new FamilySignUpDTO();
        signUpData.setNickName(nickname);  //nickname 저장

        if (profileIMG != null){
            signUpData.setProfileIMG(profileIMG.getBytes());
        }else{
            signUpData.setProfileIMG(null);
        } //이미지 저장, 없을시 null로 처리

        signUpData.setIntroduce(introduce); //intro 추가
        signUpData.setUserSeq(userSeq); // userSeq추가
        log.info("FamilySignUp data Check - Stat");
        //Validation Start
        String returnValidText  = familyValidator.familySignUpValidate(signUpData);
        //Validation End
        log.info("FamilySignUp data Check - End");

        if("Success".equals(returnValidText)){

            // Service (Create Logic Start)
            boolean checkNickNameExists = familyNicknameChecker.doesNickNameExist(nickname); //nickName 존재하는 지
            boolean checkUserSeqExists = familyNicknameChecker.doesUserSeqExist(userSeq); //user 존재하는 지
            if (!checkNickNameExists && checkUserSeqExists){
                log.info("FamilySignUp service logic Start");
                String return_text = familyService.family_signUp_service(signUpData);
                log.info("FamilySignUp service logic end");
                ResponseEntity<String> responseEntity;
                if ("Success".equals(return_text)) {
                    log.info("FamilySignUp service Success , {}", signUpData.getNickName());
                    // 회원가입 성공한 경우 처리
                    responseEntity = ResponseEntity.ok("signUp Success");
                } else if ("DataAccessException".equals(return_text)) {
                    log.info("FamilySignUp service DataAccessException , {}", signUpData.getNickName());
                    // 데이터베이스 예외 발생한 경우 처리
                    responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataAccessException");
                } else if ("RuntimeException".equals(return_text)) {
                    log.info("FamilySignUp service RuntimeException , {}", signUpData.getNickName());
                    // 런타임 예외 발생한 경우 처리
                    responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RuntimeException");
                } else {
                    log.info("FamilySignUp service don't Know Error , Please contact about Developer, {}", signUpData.getNickName());
                    // 기타 예외나 다른 경우 처리
                    responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("don't Know Error");
                }

                // Service (Create Logic End)
                return responseEntity;
            }else{
                if (checkNickNameExists){
                    log.info("FamilySignUp validation failed: already nickName in Database");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("already nickName in Database");
                }else{
                    log.info("FamilySignUp validation failed: already userSeq in Database");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not exists userSeq in Database");
                }

            }
        }else{
            log.info("FamilySignUp validation failed: {}", returnValidText);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnValidText);
        }




    }

}
