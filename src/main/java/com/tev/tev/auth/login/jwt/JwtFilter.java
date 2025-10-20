package com.tev.tev.auth.login.jwt;

import com.tev.tev.auth.login.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER="Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    private final TokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    // 실제 필터링 로직 수행
    // JWT 토큰의 인증 정보를 SecurityContext 에 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException{

        // 1. Request Header에서 access Token 을 꺼냄.
        String jwt = resolveToken(request);

        // 2. validateToken으로 토큰 유효성 검사
        if(StringUtils.hasText(jwt)) {
            if(tokenProvider.validateToken(jwt) && !tokenBlacklistService.isBlacklisted(jwt)){
                // 해당 토큰의 Authentication 조회 후 SecurityContext 에 저장
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Save authentication in SecurityContextHolder.");
            } else {
                // 토큰이 있지만 유효하지 않거나 블랙리스트에 있는 경우
                log.info("Invalid or blacklisted token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        // 토큰이 없으면 그냥 통과 (permitAll 경로를 위해)
        filterChain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }
}
