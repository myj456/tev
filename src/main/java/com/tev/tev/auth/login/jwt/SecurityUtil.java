package com.tev.tev.auth.login.jwt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

// 현재 인증된 사용자의 이름을 가져오는 유틸리티 클래스임.
// SecurityContextHolder에서 인증 정보를 추출 후 사용자의 이름을 반환.
public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil(){}

    // 현재 인증된 사용자의 이름을 Optional로 반환
    public static Optional<String> getCurrentUsername(){
        // SecuritycontextHolder에서 인증 객체 가져오기
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 객체가 없는 경우 빈 Optional 반환
        if(authentication == null || authentication.getName() == null){
            logger.debug("No authentication information");
            return Optional.empty();
        }

        String username = null;

        // 인증 객체에 Principal 타입에 따라 사용자 이름 가져오기
        if(authentication.getPrincipal() instanceof UserDetails springSecuritytUser){
            // UserDetails 타입인 경우 getUsername()을 통해 이름 추출
            username = springSecuritytUser.getUsername();
        } else if(authentication.getPrincipal() instanceof String){
            // Principal이 문자열인 경우 직접 사용자 일므으로 캐스팅
            username = (String) authentication.getPrincipal();
        }

        // Optional 반환하여 null 처리 가능하도록 함.
        return Optional.ofNullable(username);
    }
}
