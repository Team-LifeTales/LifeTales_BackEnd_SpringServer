package com.LifeTales.global.config;

import com.LifeTales.domain.user.domain.UserRole;
import com.LifeTales.domain.user.repository.UserRepository;
import com.LifeTales.domain.user.service.UserService;
import com.LifeTales.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final String secretKey;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization : {}" ,authorization);

        //token
        String token = authorization;

        if(authorization == null){
            log.error("인증토큰 확인하세요");
            filterChain.doFilter(request , response);
            return;
        }


        if(JwtUtil.isExpired(token,secretKey)){
            log.info("Token 기간만료");
            filterChain.doFilter(request , response);
            return;
        }


        String id = JwtUtil.getUserId(token ,secretKey); //공백에서 찾아서 넣어줌..
        log.info("userName = {}",id);
        /**
         * role 찾기용 .. 추가 필요한 데이터는 이런 방식으로 토큰에 추가해서 주면되나
         */
        UserRole userRole = userRepository.findById(id).getRole();
        String tokenUserRole = String.valueOf(userRole);


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(id, null , List.of(new SimpleGrantedAuthority(tokenUserRole)));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
