package com.tev.tev.auth.join.conroller;

import com.tev.tev.auth.user.dto.UserCreate;
import com.tev.tev.auth.user.repository.UserRepository;
import com.tev.tev.common.ApiResponse;
import com.tev.tev.auth.join.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class JoinController {

    private final UserRepository userRepository;
    private final JoinService joinService;

    // 유저 회원가입(생성)
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> userCreate(@Valid @RequestBody UserCreate userCreate){
        // 이메일 중복 처리
        if(userRepository.existsByEmail(userCreate.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.
                            error("중복된 이메일 입니다.", userCreate.getEmail()));
        }

        // 닉네임 중복 처리
        if(userRepository.existsByNickname(userCreate.getNickname())){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.
                            error("중복된 닉네임 입니다.", userCreate.getNickname()));
        }

        String nickname = joinService.userCreate(userCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .success("가입에 성공하셨습니다! " + nickname + "님"));
    }
}
