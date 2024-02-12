package com.userSystem.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRo {
    private Long id;
    private String account;
    private String password;
    private String username;
    private String newPassword;
    private String email;
    private String OTPCode;
}

