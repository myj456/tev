package com.tev.tev.auth.user.dto;

import com.tev.tev.auth.user.entity.Block;
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
public class UserBlockCreate {
    private Integer userId;
    private String reason;
    private LocalDateTime expiryAt;

    public Block toEntity(User user){
        return Block.builder()
                .reason(this.reason)
                .blockAt(LocalDateTime.now())
                .expiryAt(this.expiryAt)
                .user(user)
                .build();
    }
}
