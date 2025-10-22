package com.tev.tev.auth.admin.service;

import com.tev.tev.auth.user.dto.UserDetailResponse;
import com.tev.tev.auth.user.dto.UserListResponse;
import com.tev.tev.auth.user.entity.User;
import com.tev.tev.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // 유저 정보 전체 조회 - 오프셋
    public List<UserListResponse> getUserList(String name, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        List<User> userList;
        if(!StringUtils.hasText(name)) {
            userList = userRepository.findAllByOrderByUserIdDescCreatedAtDesc(pageable);
            return userList.stream()
                    .map(UserListResponse::from)
                    .toList();
        }

        userList = userRepository.findByNicknameContainingOrEmailContainingOrderByUserIdDescCreatedAtDesc(name, name, pageable);
        return userList.stream()
                .map(UserListResponse::from)
                .toList();
    }

    // 유저 상세 정보 조회
    public UserDetailResponse getUserDetails(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + userId));

        return UserDetailResponse.from(user);
    }
}
