package com.tev.tev.auth.common.dto;

import lombok.Data;

@Data
public class UserLogin {
    private String email;
    private String password;
}
