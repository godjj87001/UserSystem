package com.userSystem.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//Resource Object
public class UserRo {
    private Long id;
    private String account;
    private String password;
    private String username;
    private String email;
    private String newPassword; //change password
    private String confirmPassword; //check newPassword = confirmPassword
    private String accessToken; // jwt
}