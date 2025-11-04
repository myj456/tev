package com.tev.tev.auth.user.dto;

import com.tev.tev.auth.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserListResponse {
    private Integer userId;
    private String email;
    private String nickname;
    private String role;
    private String createdAt;
    private String expiryAt;

    public static UserListResponse from(User user){
        String expiryAt = null;

        if(user.getBlock() != null){
            if(user.getBlock().getExpiryAt() != null){
                expiryAt = user.getBlock().getExpiryAt().toString();
            }
        }

        return UserListResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .expiryAt(expiryAt)
                .build();
    }
}
