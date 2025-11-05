package com.tev.tev.auth.user.dto;

import com.tev.tev.auth.user.entity.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBlockResponse {
    private String message;
    private String reason;
    private LocalDateTime expiryAt;
    private String nickname;
    private Integer userId;

    public static UserBlockResponse from(String message, Block block){

        return UserBlockResponse.builder()
                .message(message)
                .reason(block.getReason())
                .expiryAt(block.getExpiryAt())
                .nickname(block.getUser().getNickname())
                .userId(block.getUser().getUserId())
                .build();
    }
}
