package com.LifeTales.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    public static String getUserId(String token , String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("id" , String.class);
    }

    public static boolean isExpired(String token , String secretKey){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public static  String createJwtToken(String id , String secretKey , long expiredMs){
        Claims claims = Jwts.claims();
        claims.put("id" , id);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return "Bearer " + token; // "Bearer " 접두사 추가
    }
}
