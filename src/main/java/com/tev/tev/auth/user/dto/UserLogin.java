package com.tev.tev.auth.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLogin {
    @Email
    @NotNull(message = "이메일을 작성해주세요.")
    @NotBlank(message = "이메일을 작성해주세요.")
    private String email;

    @NotNull(message = "비밀번호를 작성해주세요.")
    @NotBlank(message = "비밀번호를 작성해주세요.")
    private String password;
}
