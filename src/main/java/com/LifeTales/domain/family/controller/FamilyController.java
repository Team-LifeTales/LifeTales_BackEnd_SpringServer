package com.LifeTales.domain.family.controller;

import com.LifeTales.common.User.FamilyNicknameChecker;
import com.LifeTales.common.User.UserIdChecker;
import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.family.repository.DAO.FamilyDataDAO;
import com.LifeTales.domain.family.repository.DTO.FamilySignUpDTO;
import com.LifeTales.domain.family.service.FamilyService;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.global.Validator.FamilySignUpValidator;
import com.LifeTales.global.util.UseTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/family")
@CrossOrigin(origins = {"http://192.168.35.121:3000", "http://3.39.251.34:3000"})
public class FamilyController {
    private final ObjectMapper objectMapper;
    private final FamilyService familyService;
    private final FamilySignUpValidator familyValidator;
    private final UseTokenUtil tokenUtil;
    private final UserIdChecker userIdChecker;
    private final UserRepository userRepository;
    private final FamilyNicknameChecker familyNicknameChecker;
    public FamilyController(ObjectMapper objectMapper, FamilyService familyService, FamilySignUpValidator familyValidator, UseTokenUtil tokenUtil, UserIdChecker userIdChecker, UserRepository userRepository, FamilyNicknameChecker familyNicknameChecker) {
        this.objectMapper = objectMapper;
        this.familyService = familyService;
        this.familyValidator = familyValidator;
        this.tokenUtil = tokenUtil;
        this.userIdChecker = userIdChecker;
        this.userRepository = userRepository;
        this.familyNicknameChecker = familyNicknameChecker;
    }

    @PostMapping("/createFamily/detail")
    public ResponseEntity FamilySignUp(@RequestParam("nickname") String nickname,
                                            @RequestParam(value = "profileIMG", required = false) MultipartFile profileIMG,
                                            @RequestParam("introduce") String introduce,
                                            @RequestParam("familySignInQuestion") String familySignInQuestion,
                                            @RequestParam("familySignInAnswer") String familySignInAnswer,
                                            @RequestParam("userId") String userId) throws IOException {
        log.info("basicFamilySignUp-Start >> nickName : {}" , nickname);

        FamilySignUpDTO signUpData = new FamilySignUpDTO();
        signUpData.setNickName(nickname);  //nickname 저장
        signUpData.setFamilySignInQuestion(familySignInQuestion);
        signUpData.setFamilySignInAnswer(familySignInAnswer);

        if (profileIMG != null){
            signUpData.setProfileIMG(profileIMG.getBytes());
        }else{
            signUpData.setProfileIMG(null);
        } //이미지 저장, 없을시 null로 처리

        signUpData.setIntroduce(introduce); //intro 추가
        signUpData.setUserId(userId); // userId추가
        log.info("FamilySignUp data Check - Stat");
        //Validation Start
        String returnValidText  = familyValidator.familySignUpValidate(signUpData);
        //Validation End
        log.info("FamilySignUp data Check - End");

        if("Success".equals(returnValidText)){

            // Service (Create Logic Start)
            boolean checkNickNameExists = familyNicknameChecker.doesNickNameExist(nickname); //nickName 존재하는 지
            boolean checkUserSeqExists = userIdChecker.doesIdExist(userId); //user 존재하는 지
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

    @ResponseBody
    @GetMapping("home/")
    public ResponseEntity FamilyHomeData(HttpServletRequest request) throws IOException {

        String id = tokenUtil.findUserIdForJWT(request);
        if(userIdChecker.doesIdExist(id)){
            User user = userRepository.findById(id);
            if (user.getFamilySeq()!= null){
                Family family = user.getFamilySeq();
                log.info("lifeTalesFamilyDataGetTest >> id : {}" , family.getNickName());
                FamilyDataDAO familyDataDAO = familyService.getDataForFamily(family.getNickName());
                if(familyDataDAO == null){
                    log.info("null >> ");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 아이디");
                }else{
                    String json = objectMapper.writeValueAsString(familyDataDAO);
                    log.info(json);
                    return ResponseEntity.ok(json);
                }
            }
            else{
                log.info("family not exists");
            }

        }else{
            log.info("user not exists");
        }
        return null;
    }
}
