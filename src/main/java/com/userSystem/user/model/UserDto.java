package com.userSystem.user.model;

import com.userSystem.UtilService;
import lombok.Data;

@Data
public class UserDto extends UserRo {
    private Long id;
    private String account;
    private String password;
    private String username;
    private String newPassword; // ???K?X
    private String email;

    public UserDto() {
        // Default constructor
    }

    public UserDto(UserRo userRo, UtilService utilService, String salt, int iterations, int keyLength) {
        this.setId(userRo.getId());
        this.setAccount(userRo.getAccount());
        this.setPassword(userRo.getPassword());
        this.setUsername(userRo.getUsername());
        this.setNewPassword(userRo.getNewPassword());
        this.setEmail(userRo.getEmail());
        // Hash the passwords
        this.setPassword(utilService.hashWithPBKDF2(userRo.getPassword(), salt, iterations, keyLength));
        this.setNewPassword(utilService.hashWithPBKDF2(userRo.getNewPassword(), salt, iterations, keyLength));
    }
}

