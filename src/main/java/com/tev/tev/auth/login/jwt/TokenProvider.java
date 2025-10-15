package com.tev.tev.auth.login.jwt;

import com.tev.tev.auth.login.jwt.entity.RefreshToken;
import com.tev.tev.auth.login.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// 토큰 생성 및 토큰 안에 있는 유저 정보 추출
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Secret 키 디코딩하여 JWT 서명에 사용할 키 객체를 생성
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // jwt 토큰 생성
    public String createToken(Authentication authentication, boolean isAccessToken){

        // 사용자의 권한 정보를 문자열로 변환
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 현재 시간 & 만료 시간 설정
        long now = (new Date()).getTime();
        long expiryDate = isAccessToken ? now + ACCESS_TOKEN_EXPIRE_TIME : now + REFRESH_TOKEN_EXPIRE_TIME;
        Date validity = new Date(expiryDate); // 만료 시간 설정

        // JWT 빌더를 통해 토큰 생성
        return Jwts.builder()
                .setSubject(authentication.getName()) // 사용자 정보 설정 (subject 부분)
                .claim(AUTHORITIES_KEY, authorities) // 권한 정보를 claims 에 저장
                .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘과 키 설정
                .setExpiration(validity) // 만료 시간 설정
                .compact();
    }

    // Refresh Token 생성 후 DB에 저장
    // @Param authentication 인증 정보를 포함한 Authentication 객체
    public String createAndPersistRefreshTokenForUser(Authentication authentication){
        String refreshToken = this.createToken(authentication, false); // RefreshToken 생성

        // 만료 날짜 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        Instant instant = validity.toInstant();
        LocalDateTime expiryDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // RefreshToken 엔티티 생성 및 저장
        String email = authentication.getName();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .email(email)
                .token(refreshToken)
                .expiryDate(expiryDate)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    // 인증 정보 반환
    public Authentication getAuthentication(String token){
        // 서명 키를 사용하여 JWT 파싱 후 클레임 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 권한 정보 추출
        Collection<? extends  GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 인증 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // JWT 토큰의 유효성 검사
    public boolean validateToken(String token){
        try{
            // 서명 키를 사용하여 JWT 파싱 후 유효성 검증
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        } catch(ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false; // 토큰이 유효하지 않은 경우
    }
}
