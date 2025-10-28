package com.tev.tev.config;

import com.tev.tev.auth.admin.service.AdminService;
import com.tev.tev.auth.login.service.TokenBlacklistService;
import com.tev.tev.auth.login.jwt.JwtAccessDeniedHandler;
import com.tev.tev.auth.login.jwt.JwtAuthenticationEntryPoint;
import com.tev.tev.auth.login.jwt.TokenProvider;
import com.tev.tev.auth.login.jwt.config.JwtSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CorsConfig corsConfig;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final TokenBlacklistService tokenBlacklistService;

    private final AdminService adminService;

    // Bcrypt 암호화 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 기본 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // form 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // 세션 Stateless로 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 로그인 및 회원가입 api 요청 허용 (jwt 토큰 X)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                // exception handling 할 시 커스텀 클래스 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스 적용
                .with(new JwtSecurityConfig(tokenProvider, tokenBlacklistService, adminService), customizer -> {});
        return http.build();
    }
}
