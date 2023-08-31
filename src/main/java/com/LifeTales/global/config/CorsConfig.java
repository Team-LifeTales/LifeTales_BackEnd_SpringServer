package com.LifeTales.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://172.20.144.1:3000"); //프론트 개발자용
        config.addAllowedOrigin("http://3.39.251.34:3000"); //서버 접속용
        config.addAllowedMethod("GET"); // 허용할 HTTP 메소드
        config.addAllowedMethod("POST");
        // 다른 허용할 설정도 추가할 수 있습니다.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}