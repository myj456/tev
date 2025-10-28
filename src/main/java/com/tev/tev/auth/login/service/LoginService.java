package com.tev.tev.auth.login.service;

import com.tev.tev.auth.login.jwt.TokenProvider;
import com.tev.tev.auth.login.jwt.dto.RefreshTokenRequest;
import com.tev.tev.auth.login.jwt.dto.TokenDto;
import com.tev.tev.auth.login.jwt.dto.TokenResponse;
import com.tev.tev.auth.user.dto.UserLogin;
import com.tev.tev.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenProvider tokenProvider; // jwt 생성 + 유효성 검사 클래스
    private final AuthenticationManagerBuilder authenticationManagerBuilder; //  Spring Security 의 인증 관리자 빌더
    private final TokenBlacklistService tokenBlacklistService;

    private final UserRepository userRepository;

    // 로그인 요청 시 토큰 발급
    public Optional<TokenResponse> makeTokens(UserLogin userLogin){
        log.info("token issued");

        // 이메일 + 비밀번호로 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());

        // 인증 수행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("email=" + authentication.getName());

        // Security Context 에 인증된 사용자 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // access Token 생성
        String accessToken = tokenProvider.createToken(authentication, true);

        // refresh Token 생성 및 DB 저장
        String refreshToken = tokenProvider.createAndPersistRefreshTokenForUser(authentication);

        // 토큰 응답 객체 생성
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        // TokenResponse 객체를 Optional로 반환
        return Optional.of(tokenResponse);
    }

    // access Token 재발급
    @Transactional
    public Optional<TokenDto> makeNewAccessToken(RefreshTokenRequest request,
                                                 Authentication authentication){
        String refreshTokenValue = request.getRefreshToken();
        log.info("유저의 refresh token value=" + refreshTokenValue);

        // redis 기반 refresh Token 조회
        if(!tokenProvider.validateRefreshToken(refreshTokenValue)){
            log.info("Invalid or expired refresh token");
            return Optional.empty();
        }
        // 새로운 access Token 생성
        String accessToken = tokenProvider.createToken(authentication, true);

        TokenDto tokenDto = new TokenDto(accessToken);
        return Optional.of(tokenDto);
    }

    // 사용자 nickname 반환
    public String getUsername(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("\"존재하지 않는 이메일 정보입니다. email=" + email))
                .getNickname();
    }

    // 로그아웃
    public void logout(String accessToken, String email){
        tokenProvider.deleteRefreshToken(email);
        tokenBlacklistService.addBlacklist(accessToken);
    }
}
