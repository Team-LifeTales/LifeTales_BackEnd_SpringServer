package com.LifeTales.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        return httpSecurity
                .httpBasic().disable() //우리는 토큰으로할것
                .csrf().disable() //(!)나중에 풀어줄것
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/basic/login/" , "/api/v1/users/basic/signUp/detail/" , "/api/v1/users/basic/signUp/profile_introduce/").permitAll()
                //"/api/v1/**"
                .antMatchers(HttpMethod.POST , "/api/v1/feed/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용하는경우 이렇게 하는거라고함
                .and()
                //.addFilterBefore(new ) //jwt Token 용
                .build();
     }
}
