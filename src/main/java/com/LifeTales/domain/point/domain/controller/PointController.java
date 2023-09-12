package com.LifeTales.domain.point.domain.controller;

import com.LifeTales.domain.point.domain.DTO.RequestDetailPointDTO;
import com.LifeTales.domain.point.service.PointService;
import com.LifeTales.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/point")
@CrossOrigin(origins = {"http://192.168.35.174:3000", "http://3.39.251.34:3000"})
public class PointController {
    private final PointService pointService;
    private final UserUtil userUtil;
    public PointController(PointService pointService, UserUtil userUtil) {
        this.pointService = pointService;
        this.userUtil = userUtil;
    }

    @PostMapping("/test")
    public ResponseEntity pointTest(@RequestBody RequestDetailPointDTO detailPointDTO){
        log.info("pointTest : {}" , detailPointDTO.getUserId());
        if(detailPointDTO.isValid() && userUtil.userExsitsByID(detailPointDTO.getUserId())){
            log.info("valid success");
            String return_text = pointService.request_detail_point_service(detailPointDTO);
            if(return_text.equals("success")){
                return ResponseEntity.ok("point test success");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(return_text);
            }
        }else{
            log.info("valid fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("valid fail");
        }
    }
}
