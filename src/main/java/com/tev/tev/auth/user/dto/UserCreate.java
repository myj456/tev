package com.tev.tev.auth.user.dto;

import com.tev.tev.auth.user.entity.Roles;
import com.tev.tev.auth.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Valid
@Builder
public class UserCreate {

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotNull(message = "이메일은 필수 값입니다.")
    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 값입니다.")
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;

    @NotNull(message = "닉네임은 필수 값입니다.")
    @NotBlank(message = "닉네임은 필수 값입니다.")
    private String nickname;

    // dto -> entity
    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .nickname(this.nickname)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss")))
                .role(Roles.ROLE_USER)
                .build();
    }
}
