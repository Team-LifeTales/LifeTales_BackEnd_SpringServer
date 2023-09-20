package com.LifeTales.domain.user.service;

import com.LifeTales.domain.user.domain.Admin;
import com.LifeTales.domain.user.domain.AdminRole;
import com.LifeTales.domain.user.repository.AdminRepository;
import com.LifeTales.domain.user.repository.DTO.admin.UserAdminLoginDTO;
import com.LifeTales.domain.user.repository.DTO.admin.UserAdminSignInDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean admin_login_service(UserAdminLoginDTO userAdminLoginDTO){
        log.info("admin_login_service : {}" , userAdminLoginDTO.getId());
        String pwd ;
        if(adminRepository.existsByIdAndIsDELETED(userAdminLoginDTO.getId() , false)){
            try {
                pwd = adminRepository.findById(userAdminLoginDTO.getId()).get().getPwd();
                if(passwordEncoder.matches(userAdminLoginDTO.getPwd(), pwd)){
                    log.info("admin_login_service Login Success: {}",userAdminLoginDTO.getId());
                    return true;
                }else{
                    log.info("admin_login_service Login fail: {}",userAdminLoginDTO.getId());
                    return false;
                }
            }catch (Exception e){
                log.warn("fail admin_Login_access : ");
                log.warn("{}",e);
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean admin_register_service(UserAdminSignInDTO userAdminSignInDTO){
        AdminRole role;
        String encodedPassword = passwordEncoder.encode(userAdminSignInDTO.getPwd());
        if(userAdminSignInDTO.getRole().equals("DEVELOPER")){
            role = AdminRole.valueOf("DEVELOPER");
        }else{
            role = AdminRole.valueOf("CommunicationTeam");
        }

        try{
            adminRepository.save(Admin.builder()
                    .id(userAdminSignInDTO.getId())
                    .pwd(encodedPassword)
                    .email(userAdminSignInDTO.getEmail())
                    .role(role)
                    .build());
            return true;

        }catch (Exception e){
            log.warn("{}",e);
            return false;
        }
    }
}
