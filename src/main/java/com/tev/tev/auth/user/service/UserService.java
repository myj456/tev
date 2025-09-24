package com.tev.tev.auth.user.service;

import com.tev.tev.auth.common.dto.UserLogin;
import com.tev.tev.auth.common.dto.UserRequest;
import com.tev.tev.auth.common.entity.User;
import com.tev.tev.auth.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 유저 회원가입(생성)
    public String userCreate(UserRequest userRequest){
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .nickname(userRequest.getNickname())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss")))
                .role("ADMIN")
                .build();

        return userRepository.save(user).getNickname();
    }

    public String userLogin(UserLogin userLogin){
        // TODO: 커스텀 예외 처리로 변경
        if(!userRepository.existsByEmail(userLogin.getEmail())){
            return null;
        }

        User user = userRepository.findByEmail(userLogin.getEmail());

        if(!user.getPassword().equals(userLogin.getPassword())){
            return null;
        }

        return user.getNickname();
    }
}
