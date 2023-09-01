package com.LifeTales.domain.user.controller;

import com.LifeTales.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/basic")
@CrossOrigin(origins = {"http://172.20.144.1:3000", "http://3.39.251.34:3000"})
public class BasicUserUpdateController {
    private final UserService userService;


    public BasicUserUpdateController(UserService userService) {
        this.userService = userService;
    }




}
