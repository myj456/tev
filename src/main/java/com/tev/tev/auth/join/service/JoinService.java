package com.tev.tev.auth.join.service;

import com.tev.tev.auth.user.dto.UserCreate;
import com.tev.tev.auth.user.entity.Roles;
import com.tev.tev.auth.user.entity.User;
import com.tev.tev.auth.user.repository.UserRepository;
import com.tev.tev.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void checkUser(String email, String nickname){
        if(userRepository.existsByEmail(email)){
            throw new DuplicateResourceException("이미 사용중인 이메일 입니다.");
        }
        if(userRepository.existsByNickname(nickname)){
            throw new DuplicateResourceException("이미 사용중인 이메일 입니다.");
        }
    }

    // 유저 회원가입(생성)
    public String userCreate(UserCreate userCreate){
        checkUser(userCreate.getEmail(), userCreate.getNickname());

        User user = userCreate.toEntity(passwordEncoder);

        return userRepository.save(user).getNickname();
    }



}
