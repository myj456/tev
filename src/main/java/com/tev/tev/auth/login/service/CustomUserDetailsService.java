package com.tev.tev.auth.login.service;

import com.tev.tev.auth.login.CustomUserDetails;
import com.tev.tev.auth.user.entity.User;
import com.tev.tev.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        // 이메일을 통해 사용자 정보 존재 확인
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 정보 입니다. email=" + username));

        return CustomUserDetails.from(user);
    }
}
