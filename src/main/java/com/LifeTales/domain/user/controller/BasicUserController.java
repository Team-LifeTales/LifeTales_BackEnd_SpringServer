package com.LifeTales.domain.user.controller;

import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.user.repository.DTO.UserSignInDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpDTO;
import com.LifeTales.domain.user.repository.DTO.UserSignUpStep2DTO;
import com.LifeTales.domain.user.service.UserService;
import com.LifeTales.global.Validator.UserSignUpValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/basic")
public class BasicUserController {
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final UserSignUpValidator uservalidator;

    private final UserIdChecker userIdChecker;
    public BasicUserController(ObjectMapper objectMapper, UserService userService, UserSignUpValidator uservalidator, UserIdChecker userIdChecker) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.uservalidator = uservalidator;
        this.userIdChecker = userIdChecker;
    }

    @PostMapping("/login/")
    public ResponseEntity<String> basicUserLogin(UserSignInDTO userSignInDTO){
        log.info("basicUserLogin >> {}" , userSignInDTO.getId());
        String token  = userService.login(userSignInDTO.getId() , userSignInDTO.getPassWord());

        return ResponseEntity.ok(token);
    }



    @PostMapping("/signUp/detail/")
    public ResponseEntity basicUserSignUp(@RequestBody UserSignUpDTO signUpData) {
        log.info("basicUserSignUp Start - need Data \nid : {} , PWD : {}, NickName : {} , " +
                "Name : {} , Birthday ; {} , PhoneNumber : {} , email : {}" +
                "",signUpData.getId(),signUpData.getPwd(),signUpData.getNickName(),signUpData.getNickName()
        ,signUpData.getName(),signUpData.getBirthDay() , signUpData.getPhoneNumber() , signUpData.getEmail());
        log.info("UserSignUp data Check - Start");
        //Validation Start
        String returnValidText  = uservalidator.userSignUpValidate(signUpData);
        //Validation End
        log.info("UserSignUp data Check - End");

        if("Success".equals(returnValidText)){

            // Service (Create Logic Start)

            log.info("UserSignUp service logic Start");
            String return_text = userService.user_signUp_service(signUpData);
            log.info("UserSignUp service logic end");

            ResponseEntity<String> responseEntity;
            if ("Success".equals(return_text)) {
                log.info("UserSignUp service Success , {}", signUpData.getId());
                // 회원가입 성공한 경우 처리
                responseEntity = ResponseEntity.ok("signUp Success");
            } else if ("DataAccessException".equals(return_text)) {
                log.info("UserSignUp service DataAccessException , {}", signUpData.getId());
                // 데이터베이스 예외 발생한 경우 처리
                responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DataAccessException");
            } else if ("RuntimeException".equals(return_text)) {
                log.info("UserSignUp service RuntimeException , {}", signUpData.getId());
                // 런타임 예외 발생한 경우 처리
                responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RuntimeException");
            } else {
                log.info("UserSignUp service don't Know Error , Please contact about Developer, {}", signUpData.getId());
                // 기타 예외나 다른 경우 처리
                responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("don't Know Error");
            }

            // Service (Create Logic End)
            return responseEntity;

        }else{
            log.info("UserSignUp validation failed: {}", returnValidText);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnValidText);
        }




    }

    @PostMapping("/signUp/profile_introduce/")
    public ResponseEntity basicUserSignUpProfileUpload(@RequestParam("profileIMG") MultipartFile profileIMG,
                                                       @RequestParam("id") String id,
                                                       @RequestParam("intro") String intro) throws IOException {
        /**
         * do
         * id checker - 존재하는지 확인 id
         * 프로파일이미지 존재 여부 - 없는 경우도 가능하게
         * 소개글 확인
         */
        log.info("basicUserSignUpProfileUpload >> id : {}" , id);
        UserSignUpStep2DTO signUpData = new UserSignUpStep2DTO();
        signUpData.setId(id);
        signUpData.setProfileIMG(profileIMG.getBytes());
        signUpData.setIntro(intro);

        boolean checkIdExists = userIdChecker.doesIdExist(id);
        if(checkIdExists){
            log.info("basicUserSignUpProfileUpload >> id checker success -> Start logic-> 아이디 >> id : {}" , signUpData.getId());
            if(signUpData.getIntro() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("소개글은 필수 입니다.");
            }else{
                //user Sign up step 2 Start
                String returnText = userService.user_signUp_step2_service(signUpData);
                if (returnText.equals("Success")){
                    return ResponseEntity.ok("signUp Success");
                }else if (returnText.equals("Error - merge Error")){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에러 - db");
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에러");
                }

            }
        }else{
            log.info("basicUserSignUpProfileUpload >> fail -> 존재하지 않는 아이디 >> id : {}" , signUpData.getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
        }

    }


}
