package com.tev.tev.auth.common.dto;

import com.tev.tev.auth.common.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String email;
    private String password;
    private String nickname;

    // dto -> entity
    public User toEntity(){
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
