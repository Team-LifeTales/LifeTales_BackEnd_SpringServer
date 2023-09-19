package com.LifeTales.domain.user.controller;

import com.LifeTales.domain.user.repository.DTO.UserAdminSignInDTO;
import com.LifeTales.domain.user.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/admin")
@CrossOrigin(origins = {"*"})
public class AdminUserController {

    private final AdminService adminService;

    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(UserAdminSignInDTO userAdminSignInDTO) {
        if(userAdminSignInDTO.nullCheck()){
            return ResponseEntity.badRequest().body("fail : empty-data");
        }
        if(userAdminSignInDTO.isValied()){
            return ResponseEntity.badRequest().body("fail : unValid-data");
        }
        if(adminService.admin_register_service(userAdminSignInDTO)){
            // 가입 성공 시 200 OK 응답 반환
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail : server Error - please contact - developer");
        }
    }

}
