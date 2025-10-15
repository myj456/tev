package com.tev.tev.auth.login.service;

import com.tev.tev.auth.login.jwt.TokenProvider;
import com.tev.tev.auth.login.jwt.dto.RefreshTokenRequest;
import com.tev.tev.auth.login.jwt.dto.TokenDto;
import com.tev.tev.auth.login.jwt.dto.TokenResponse;
import com.tev.tev.auth.login.jwt.entity.RefreshToken;
import com.tev.tev.auth.login.jwt.repository.RefreshTokenRepository;
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

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final TokenProvider tokenProvider; // jwt 생성 + 유효성 검사 클래스
    private final AuthenticationManagerBuilder authenticationManagerBuilder; //  Spring Security 의 인증 관리자 빌더

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

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

        // DB에서 refresh Token 조회
        RefreshToken validRefreshToken =
                refreshTokenRepository.findById(refreshTokenValue)
                        .orElseThrow(() -> new IllegalArgumentException("invalid Refresh token"));

        TokenDto tokenDto = null;

        // refresh Token 만료 시 삭제한 후 null 반환
        if(isTokenExpired(validRefreshToken)){
            refreshTokenRepository.delete(validRefreshToken);
            return Optional.empty();
        }
        log.info("DB refresh token value=" + validRefreshToken.getToken());

        // 새로운 access Token 생성
        String accessToken = tokenProvider.createToken(authentication, true);

        tokenDto = new TokenDto(accessToken);
        return Optional.ofNullable(tokenDto);
    }

    // refresh Token 만료 여부
    public boolean isTokenExpired(RefreshToken refreshToken){
        return refreshToken.getExpiryDate().isBefore(LocalDateTime.now());
    }

    // 사용자 nickname 반환
    public String getUsername(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("\"존재하지 않는 이메일 정보입니다. email=" + email))
                .getNickname();
    }
}
