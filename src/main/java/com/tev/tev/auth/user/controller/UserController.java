package com.tev.tev.auth.user.controller;

import com.tev.tev.auth.common.dto.UserLogin;
import com.tev.tev.common.ApiResponse;
import com.tev.tev.auth.common.dto.UserRequest;
import com.tev.tev.auth.common.repository.UserRepository;
import com.tev.tev.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 유저 회원가입(생성)
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> userCreate(UserRequest userRequest){
        // 닉네임 중복 처리
        if(userRepository.existsByNickname(userRequest.getNickname())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.
                            error("중복된 닉네임 입니다.", userRequest.getNickname()));
        }

        String nickname = userService.userCreate(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .success("가입에 성공하셨습니다! " + nickname + "님"));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<String>> userLogin(UserLogin userLogin){
        String nickname = userService.userLogin(userLogin);

        if(nickname == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.fail("존재하지 않는 이메일이거나 비밀번호가 잘못됐습니다."));
        }

        return ResponseEntity
                .ok(ApiResponse.success("어서오세요. " + nickname + "님"));
    }

}
