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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PointService {
    private final PointRepository pointRepository;
    private final DetailPointRepository detailPointRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
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

                detailPointDTO.setDetailPoint(data.getPoint());
                detailPointDTO.setPointLog(data.getPointLog());
                if(make_detail_point_service(detailPointDTO)){
                    if(merge_point_service(point.getSeq(), detailPointDTO.getDetailPoint())){
                        //merge Success
                        return "success";
                    }else{
                        //merge fail
                        return "fail - cant merge point";
                    }
                }else{
                    //point cant find so, value is null
                    return "point cant save";
                }
            }catch (Exception e){
                log.info("{}",e);
                return "cant find pointData";
            }
        }catch (Exception e){
            log.info("{}",e);
            return "cant find userData";
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

    private boolean merge_point_service(Long pointSeq , int new_point){
        log.info("merge_point_service Start");
        if(pointRepository.existsById(pointSeq)){
            Point pointEntity = entityManager.find(Point.class,pointSeq);
            int before_point = new_point + pointEntity.getPoint();

            pointEntity.setPoint(before_point);
            try {
                entityManager.merge(pointEntity);
                log.info("merge_point_service success");
                return true;
            }catch (Exception e){
                log.info("merge_point_service fail");
                log.info("{}",e);
                return false;
            }
        }else{
            log.info("merge_point_service cant find point Entity");
            return false;
        }


    }
}
