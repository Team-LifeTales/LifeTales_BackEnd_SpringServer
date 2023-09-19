package com.LifeTales.domain.user.controller;

import com.LifeTales.domain.user.repository.DTO.UserAdminSignInDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/admin")
@CrossOrigin(origins = {"*"})
public class AdminUserController {
    @PostMapping("/register")
    public String registerAdmin(UserAdminSignInDTO userAdminSignInDTO) {
        userAdminSignInDTO.nullCheck()
        // 가입 성공 시 로그인 페이지로 리디렉션
        return "redirect:/login";
    }
}
