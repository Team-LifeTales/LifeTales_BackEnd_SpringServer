package com.LifeTales.domain.user.controller;

import com.LifeTales.domain.user.repository.DAO.admin.DeletedUserDAO;
import com.LifeTales.domain.user.repository.DTO.admin.UserAdminLoginDTO;
import com.LifeTales.domain.user.repository.DTO.admin.UserAdminSignInDTO;
import com.LifeTales.domain.user.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(UserAdminLoginDTO adminLoginDTO) {
        if(adminLoginDTO.nullCheck()){
            log.info("loginAdmin fail : null");
            return ResponseEntity.badRequest().body("fail : empty-data");
        }
        if(adminLoginDTO.isValied()){
            log.info("loginAdmin fail : vail fail");
            return ResponseEntity.badRequest().body("fail : unValid-data");
        }
        if(adminService.admin_login_service(adminLoginDTO)){
            // 가입 성공 시 200 OK 응답 반환
            return ResponseEntity.ok(adminLoginDTO.getId());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail : server Error - please contact - developer");
        }
    }

    @PostMapping("/main/userRecovery")
    public ResponseEntity<Page<DeletedUserDAO>> userRecovery_findDeleted_all_user(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNum
            , Pageable pageable) {
        log.info("userRecovery_findDeleted_all_user start");
        Page<DeletedUserDAO> deletedUserPage =  adminService.deleted_user_find_all(pageNum , pageable);
        return ResponseEntity.ok(deletedUserPage);
    }

}
