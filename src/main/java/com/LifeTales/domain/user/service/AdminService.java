package com.LifeTales.domain.user.service;

import com.LifeTales.domain.user.domain.Admin;
import com.LifeTales.domain.user.domain.AdminRole;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.AdminRepository;
import com.LifeTales.domain.user.repository.DAO.admin.DeletedUserDAO;
import com.LifeTales.domain.user.repository.DTO.admin.UserAdminLoginDTO;
import com.LifeTales.domain.user.repository.DTO.admin.UserAdminSignInDTO;
import com.LifeTales.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AdminService {
    private static final int PAGE_POST_COUNT = 10;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private static final String orderCriteria = "isDELETED";
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

    public Page<DeletedUserDAO> deleted_user_find_all(int pageNum , Pageable pageable){
        log.info("deleted_user_find_all start");
        Page<DeletedUserDAO> returnPage = null;
        Sort sort = Sort.by(Sort.Order.desc(orderCriteria));
        pageable = PageRequest.of(pageNum, PAGE_POST_COUNT, sort);
        Page<User> deletedUser = userRepository.findByIsDELETED(true , pageable);
        if(deletedUser != null){
            returnPage = deletedUser.map(userData ->{
                DeletedUserDAO deletedUserDAO = new DeletedUserDAO();
                deletedUserDAO.setId(userData.getId());
                deletedUserDAO.setName(userData.getName());
                deletedUserDAO.setNickName(userData.getNickName());
                deletedUserDAO.setEmail(userData.getEmail());
                deletedUserDAO.setPhoneNumber(userData.getPhoneNumber());

                return deletedUserDAO;
            });

            for (DeletedUserDAO deletedUserDAO : returnPage) {
                System.out.println("ID: " + deletedUserDAO.getId());
                System.out.println("Name: " + deletedUserDAO.getName());
                System.out.println("NickName: " + deletedUserDAO.getNickName());
                System.out.println("Email: " + deletedUserDAO.getEmail());
                System.out.println("PhoneNumber: " + deletedUserDAO.getPhoneNumber());
                System.out.println("--------------------------");
            }
        }else{
            log.info("cant find userDeleted");
        }


        return returnPage;
    }
}
