package com.LifeTales.domain.user.service;

import com.LifeTales.domain.user.repository.DAO.UserBasicDataDAO;
import com.LifeTales.domain.user.repository.DTO.UserBasicDataDTO;
import com.LifeTales.domain.user.repository.Mongo.UserBasicData;
import com.LifeTales.domain.user.repository.UserMongoRepository;
import com.LifeTales.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserMongoService {

    /**
     * user 에게 Data를 주기 위한 서비스 코드
     * Read
     * create
     * Update
     * delete ? -> login page access 거부 ..
     */
    private final UserRepository userRepository;
    private final UserMongoRepository userMongoRepository;

    //    public String basicUserData(String id){
//        /**
//         * mongo db에 데이터가 있는경우
//         * 데이터가 없어 데이터를 넣은뒤 보내야하는 경우
//         */
//    }

    public UserBasicDataDAO readUserBasicData(String userId) {
        log.info("ReadUserBasicData start id : {}", userId);

        if (!userMongoRepository.existsById(userId)) {
            log.info("ReadUserBasicData cant find id : {}", userId);
            return null;
        }

        try {
            log.info("ReadUserBasicData find id : {}", userId);
            Optional<UserBasicData> userBasicDataOptional = userMongoRepository.findById(userId);

            if (userBasicDataOptional.isPresent()) {
                UserBasicData findUserBasicData = userBasicDataOptional.get();
                log.info("ReadUserBasicData success find : {}", userId);

                UserBasicDataDAO dao = new UserBasicDataDAO();
                dao.setSeq(findUserBasicData.getSeq());
                dao.setId(findUserBasicData.getId());
                dao.setNickName(findUserBasicData.getNickName());
                dao.setFamilySeq(findUserBasicData.getFamilySeq());

                if (findUserBasicData.getProfile() == null) {
                    log.info("ReadUserBasicData dont have profile : {}", userId);
                    dao.setProfile("default"); // 나중에 추가
                } else {
                    log.info("ReadUserBasicData have profile id : {}", userId);
                    dao.setProfile(findUserBasicData.getProfile());
                }

                return dao;
            } else {
                log.info("ReadUserBasicData fail find : {}", userId);
                return null;
            }
        } catch (Exception e) {
            log.info("ReadUserBasicData error id : {}", userId);
            log.warn("{}", e);
            return null;
        }
    }

    public boolean createUserBasicData(UserBasicDataDTO dataDTO) {
        log.info("uploadUserBasicData start for ID: {}", dataDTO.getId());

        try {
            if (userMongoRepository.existsById(dataDTO.getId())) {
                log.info("User with ID {} already exists. Upload failed.", dataDTO.getId());
                return false;
            } else {
                UserBasicData userBasicData = mapDTOToUserBasicData(dataDTO);
                userMongoRepository.save(userBasicData);
                log.info("uploadUserBasicData success for ID: {}", dataDTO.getId());
                return true;
            }
        } catch (Exception e) {
            log.error("Error while uploading user data for ID: {}", dataDTO.getId(), e);
            return false;
        }
    }

    private UserBasicData mapDTOToUserBasicData(UserBasicDataDTO dataDTO) {
        UserBasicData userBasicData = new UserBasicData();
        userBasicData.setSeq(dataDTO.getSeq());
        userBasicData.setId(dataDTO.getId());
        userBasicData.setFamilySeq(dataDTO.getFamilySeq());
        userBasicData.setProfile(dataDTO.getProfile());
        userBasicData.setNickName(dataDTO.getNickName());
        return userBasicData;
    }
}
