package com.tev.tev.auth.login.jwt.config;

import com.tev.tev.auth.login.TokenBlacklistService;
import com.tev.tev.auth.login.jwt.JwtFilter;
import com.tev.tev.auth.login.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TokenProvider 과 JwtFilter 를 SecurityConfig 에 적용할 때 사용함.
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    // TokenProvider 주입 후 JwtFilter를 통해 Security 로직에 필터 등록
    @Override
    public void configure(HttpSecurity http){
        http.addFilterBefore(
                new JwtFilter(tokenProvider, tokenBlacklistService),
                UsernamePasswordAuthenticationFilter.class);
    }
}
