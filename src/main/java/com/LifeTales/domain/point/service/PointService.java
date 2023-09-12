package com.LifeTales.domain.point.service;

import com.LifeTales.domain.point.Repository.DetailPointRepository;
import com.LifeTales.domain.point.Repository.PointRepository;
import com.LifeTales.domain.point.domain.DTO.DetailPointDTO;
import com.LifeTales.domain.point.domain.DTO.RequestDetailPointDTO;
import com.LifeTales.domain.point.domain.DetailPoint;
import com.LifeTales.domain.point.domain.Point;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.util.UserUtil;
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
    private final UserUtil userUtil;
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
    public String request_detail_point_service(RequestDetailPointDTO data){
        /**
         * userSeq -> pointSeq find
         * point & ,
         */
        DetailPointDTO detailPointDTO = new DetailPointDTO();

        try {
            User user = userRepository.findById(data.getUserId());
            try {
                Point point = pointRepository.findByUser(user);
                detailPointDTO.setPoint(point);
            }catch (Exception e){
                log.info("{}",e);
                return "cant find pointData";
            }
        }catch (Exception e){
            log.info("{}",e);
            return "cant find userData";
        }

        detailPointDTO.setDetailPoint(data.getPoint());
        detailPointDTO.setPointLog(data.getPointLog());

        if(make_detail_point_service(detailPointDTO)){
            return "success";
        }else{
            return "point cant save";
        }
    }
    private boolean make_detail_point_service(DetailPointDTO detailPointDTO){
        log.info("make_detail_point_service start");
        try {
            detailPointRepository.save(
                    DetailPoint.builder()
                            .point(detailPointDTO.getPoint())
                            .detailPoint(detailPointDTO.getDetailPoint())
                            .pointLog(detailPointDTO.getPointLog())
                            .build()
            );
            log.info("make_detail_point_service success");
            return true;
        }catch (Exception e){
            log.info("make_detail_point_service fail");
            log.info("{}",e);
            return false;
        }

    }
}
