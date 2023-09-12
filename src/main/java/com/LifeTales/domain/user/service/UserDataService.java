package com.LifeTales.domain.user.service;

import com.LifeTales.domain.user.repository.DTO.UserBasicDataDTO;
import com.LifeTales.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserDataService {

    /**
     * user 에게 Data를 주기 위한 서비스 코드
     */
    private final UserRepository userRepository;
    public String basicUserData(String id){
        /**
         * mongo db에 데이터가 있는경우 
         * 데이터가 없어 데이터를 넣은뒤 보내야하는 경우
         */
    }


}
