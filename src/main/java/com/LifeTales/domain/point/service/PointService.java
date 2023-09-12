package com.LifeTales.domain.point.service;

import com.LifeTales.domain.point.Repository.DetailPointRepository;
import com.LifeTales.domain.point.Repository.PointRepository;
import com.LifeTales.domain.point.domain.DTO.DetailPointDTO;
import com.LifeTales.domain.point.domain.Point;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PointService {
    private final PointRepository pointRepository;
    private final DetailPointRepository detailPointRepository;

    private final UserRepository userRepository;

    public String make_user_point_store_service(String userId){
        log.info("make_user_point_store_service : user Id : {}" ,userId );
        String return_text = "";
        /**
         * user -> 유저를 찾는다.
         * user -> point user 가 있는가?
         *
         */
        User user = userRepository.findById(userId);
        if(pointRepository.existsByUser(user)){
            //user exists
            return_text = "user already exists";
            return return_text;
        }else{
            //user not exists
            try {
                pointRepository.save(
                        Point.builder()
                                .point(0)
                                .user(user)
                                .build()
                );
                return_text = "point store success";
                return return_text;
            }catch (Exception e){
                log.info("{}",e);
                return_text ="db_error";
                return return_text;
            }
        }
    }
    public void request_detail_point_service(String userId , DetailPointDTO detailPointDTO){
        /**
         * userSeq -> pointSeq find
         * point & ,
         */
    }
    public void make_detail_point_service(DetailPointDTO detailPointDTO){

    }
}
