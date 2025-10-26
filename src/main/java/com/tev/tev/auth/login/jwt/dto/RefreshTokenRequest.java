package com.tev.tev.auth.login.jwt.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    
    private String refreshToken; // 클라이언트로부터 전달받는 refresh 토큰 저장 필드
}
