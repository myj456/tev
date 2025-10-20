package com.tev.tev.auth.login.controller;

import com.tev.tev.auth.login.jwt.JwtFilter;
import com.tev.tev.auth.login.jwt.TokenProvider;
import com.tev.tev.auth.login.jwt.dto.RefreshTokenRequest;
import com.tev.tev.auth.login.jwt.dto.TokenDto;
import com.tev.tev.auth.login.jwt.dto.TokenResponse;
import com.tev.tev.auth.user.dto.UserLogin;
import com.tev.tev.common.ApiResponse;
import com.tev.tev.auth.login.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {

    private final LoginService loginService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> userLogin(@Valid @RequestBody UserLogin userLogin) {

        //.토큰 생성
        Optional<TokenResponse> tokenResponse
                = loginService.makeTokens(userLogin);

        // HttpHeaders 생성
        HttpHeaders httpHeaders = new HttpHeaders();

        // Authorization 헤더에 Bearer + AccessToken을 추가
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenResponse.get().getAccessToken());

        String username = loginService.getUsername(userLogin.getEmail());

        // 생성한 토큰을 포함한 응답을 반환
        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(ApiResponse.success("어서오세요. " + username + "님", tokenResponse.get()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7);

        String email = tokenProvider.getUsernameFromToken(accessToken);

        loginService.logout(accessToken, email);
        return ResponseEntity
                .ok(ApiResponse.success("로그아웃 성공"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(RefreshTokenRequest request,
                                                       Authentication authentication) {
        try {
            // 새로운 access Token 발급
            Optional<TokenDto> tokenDto = loginService.makeNewAccessToken(request, authentication);

            // 토큰 생성 성공 시 새 access Token 반환
            if (tokenDto.isPresent()) {
                return ResponseEntity
                        .ok(ApiResponse.success(tokenDto.get()));
            } else {
                return ResponseEntity
                        .ok(ApiResponse.fail("유효하지 않은 refresh Token 입니다. 로그인을 다시 해주세요."));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}