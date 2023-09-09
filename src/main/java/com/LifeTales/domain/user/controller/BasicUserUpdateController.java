package com.LifeTales.domain.user.controller;

import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.domain.user.service.UserService;
import com.LifeTales.global.util.UseTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/basic/update")
@CrossOrigin(origins = {"http://172.20.144.1:3000", "http://3.39.251.34:3000"})
public class BasicUserUpdateController {

    private final UseTokenUtil tokenUtil;
    private final UserService userService;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public BasicUserUpdateController(UseTokenUtil tokenUtil, UserService userService, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * password Update
     * nickName Update
     * profile Update
     * intro Update
     */

    //Password Update
    @PostMapping("/password")
    public ResponseEntity basicUserUpdatePassWord(@RequestBody User.UserUpdate__Password userData, HttpServletRequest request){
        String id = tokenUtil.findUserIdForJWT(request);
        log.info("user Password Update {}" ,id);
        if(userRepository.existsById(id)){
            //true
            if(passwordEncoder.matches(userData.getPwd() , userRepository.findById(id).getPwd() )){
                //passWord true
                String return_text = userService.update_user_service(id , "password",userData.getNewPwd());
                if((return_text).equals("success")){
                    return ResponseEntity.ok("update Success");
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not matches password");
            }

        }else{
            //false
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not exist id");
        }
    }
    @PostMapping("/nickName")
    public ResponseEntity basicUserUpdateNickName(@RequestBody User.UserUpdate__NickName userData, HttpServletRequest request){
        String id = tokenUtil.findUserIdForJWT(request);
        log.info("user NickName Update {}" , id);
        if(userRepository.existsById(id)){
            String return_text = userService.update_user_service(id , "nickName",userData.getNickName());

            if((return_text).equals("success")){
                return ResponseEntity.ok("update Success");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
            }

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not exist id");
        }
    }

    @PostMapping("/intro")
    public ResponseEntity basicUserUpdateIntro(@RequestBody User.UserUpdate__Intro userData, HttpServletRequest request){
        String id = tokenUtil.findUserIdForJWT(request);
        log.info("user Intro Update {}" , id);
        if(userRepository.existsById(id)){
            String return_text = userService.update_user_service(id , "intro",userData.getIntroduce());

            if((return_text).equals("success")){
                return ResponseEntity.ok("update Success");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
            }

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not exist id");
        }
    }

    @PostMapping("/profile")
    public ResponseEntity basicUserUpdateProfile(@RequestParam("image") MultipartFile imageFile , HttpServletRequest request) throws IOException {
        String id = tokenUtil.findUserIdForJWT(request);
        log.info("Start  basicUserUpdateProfile id : {}" , id);

        if(userRepository.existsById(id)){
            try {
                //이미지 변환
                byte[] imageData = imageFile.getBytes();
                String return_text = userService.update_profile_S3_service(id , imageData);
                if(return_text.equals("success")){
                    return ResponseEntity.ok("update Success");
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
                }
            }catch (Exception e){
                log.warn(String.valueOf(e));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("call me");
            }
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not exist id");
        }

    }




}
