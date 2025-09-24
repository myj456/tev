package com.tev.tev.auth.common.dto;

import com.tev.tev.auth.common.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Integer id;
    private String nickname;
    private String email;
    private String role;
    private String createdAt;

    // entity -> dto
    public static UserResponse from(User user){
        return UserResponse.builder()
                .id(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
