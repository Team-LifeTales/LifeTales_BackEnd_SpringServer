package com.LifeTales.domain.user.service;

import com.LifeTales.domain.user.domain.Admin;
import com.LifeTales.domain.user.domain.AdminRole;
import com.LifeTales.domain.user.repository.AdminRepository;
import com.LifeTales.domain.user.repository.DTO.UserAdminSignInDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;

    public boolean admin_register_service(UserAdminSignInDTO userAdminSignInDTO){
        AdminRole role;
        if(userAdminSignInDTO.getRole().equals("DEVELOPER")){
            role = AdminRole.valueOf("DEVELOPER");
        }else{
            role = AdminRole.valueOf("CommunicationTeam");
        }

        try{
            adminRepository.save(Admin.builder()
                    .id(userAdminSignInDTO.getId())
                    .pwd(userAdminSignInDTO.getPwd())
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
