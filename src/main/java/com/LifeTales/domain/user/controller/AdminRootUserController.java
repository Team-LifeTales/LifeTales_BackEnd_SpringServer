package com.LifeTales.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/v1/users/admin")
@CrossOrigin(origins = {"*"})
public class AdminRootUserController {
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // 회원 가입 폼 페이지로 이동
        return "registerAdmin";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // 로그인 폼 포에지 이동
        return "loginAdmin";
    }

    @GetMapping("/main")
    public String showMainForm(Model model) {
        // 로그인 폼 포에지 이동
        return "mainAdmin";
    }

    @GetMapping("/main/userRecovery")
    public String showUserRecoveryPage(Model model) {
        // 로그인 폼 포에지 이동
        return "communication/userRecovery";
    }
}

