package com.tev.tev.auth.user.dto;

import com.tev.tev.auth.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UserListResponse {
    private Integer userId;
    private String email;
    private String nickname;
    private String role;
    private String createdAt;

    public static UserListResponse from(User user){
        return UserListResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
