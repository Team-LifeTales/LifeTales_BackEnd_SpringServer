package com.LifeTales.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/users/admin")
@CrossOrigin(origins = {"*"})
public class AdminRootUserController {
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // 회원 가입 폼 페이지로 이동
        return "/v1/users/admin/register";
    }

}
