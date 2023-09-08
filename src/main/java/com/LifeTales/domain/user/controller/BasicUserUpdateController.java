package com.LifeTales.domain.user.controller;

import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.domain.user.service.UserService;
import com.LifeTales.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/basic/update")
@CrossOrigin(origins = {"http://172.20.144.1:3000", "http://3.39.251.34:3000"})
public class BasicUserUpdateController {
    private final UserService userService;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public BasicUserUpdateController(UserService userService, UserRepository userRepository) {
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
    public ResponseEntity basicUserUpdatePassWord(@RequestBody User.UserUpdate__Password userData){
        log.info("user Password Update {} {}" , userData.getId() , userData.getPwd());
        if(userRepository.existsById(userData.getId())){
            //true
            if(passwordEncoder.matches(userData.getPwd() , userRepository.findById(userData.getId()).getPwd() )){
                //passWord true
                if(userService.update_password_service(userData.getId() , userData.getNewPwd())){
                    return ResponseEntity.ok("update Success");
                }else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에러 - db");
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
    public ResponseEntity basicUserUpdateNickName(@RequestBody User.UserUpdate__Password userData){
        return null;
    }
    @PostMapping("/profile")
    public ResponseEntity basicUserUpdateProfile(@RequestBody User.UserUpdate__Password userData){
        return null;
    }
    @PostMapping("/intro")
    public ResponseEntity basicUserUpdateIntro(@RequestBody User.UserUpdate__Password userData){
        return null;
    }



}
