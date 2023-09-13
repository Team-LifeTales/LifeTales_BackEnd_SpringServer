package com.LifeTales.domain.user.controller;

import com.LifeTales.domain.user.repository.DAO.UserBasicDataDAO;
import com.LifeTales.domain.user.service.UserMongoService;
import com.LifeTales.global.util.UseTokenUtil;
import com.LifeTales.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/basic/data")
@CrossOrigin(origins = {"http://192.168.35.174:3000", "http://3.39.251.34:3000"})
public class BasicUserReadController {
    private final UserMongoService userMongoService;

    private final UseTokenUtil useTokenUtil;
    private final UserUtil userUtil;
    public BasicUserReadController(UserMongoService userMongoService, UseTokenUtil useTokenUtil, UserUtil userUtil) {
        this.userMongoService = userMongoService;
        this.useTokenUtil = useTokenUtil;
        this.userUtil = userUtil;
    }

    @GetMapping("")
    public ResponseEntity basicUserData(HttpServletRequest request){
        String userId = useTokenUtil.findUserIdForJWT(request);
        /**
         * user Id - > find mongo db -> 존재 x -> 생성요청
         */
        // 3회까지 반복해서 데이터를 조회한다.
        for (int i = 0; i < 3; i++) {
            UserBasicDataDAO data = userMongoService.readUserBasicData(userId);
            if (data != null) {
                return ResponseEntity.ok(data);
            }
        }
        // 3회 모두 데이터를 조회하지 못하면 사용자 기본 데이터를 생성한다.
        boolean isCreated = userMongoService.createUserBasicData(userId);
        if (isCreated) {
            UserBasicDataDAO data = userMongoService.readUserBasicData(userId);
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
