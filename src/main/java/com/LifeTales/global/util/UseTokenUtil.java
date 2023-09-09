package com.LifeTales.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component
public class UseTokenUtil {

    @Value("${jwt.Life-tales-secretKey}")
    private String secretKey;
    public String findUserIdForJWT( HttpServletRequest request){
        // 클라이언트에서 전송한 요청 헤더에서 토큰을 추출
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰에서 아이디 값을 추출
        String id = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring("Bearer ".length());
            id = JwtUtil.getUserId(token, secretKey);
            return id;
        }else{
            return null;
        }
    }
}
