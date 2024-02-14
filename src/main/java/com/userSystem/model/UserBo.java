package com.userSystem.model;

import com.userSystem.Service.UtilService;
import lombok.Data;

@Data
public class UserBo  {
    private Long id;
    private String account;
    private String password;
    private String username;
    private String newPassword; // §ó·s±K½X¡B§Ñ°O±K½X
    private String email;
    String salt = "yosolo";
    int iterations = 1000;
    int keyLength = 256; // in bits

    public UserBo(UserRo userRo) {
        UtilService utilService =new UtilService();
        this.setId(userRo.getId() != null ? userRo.getId() : null);
        this.setAccount(userRo.getAccount() != null ? userRo.getAccount() : null);
        this.setUsername(userRo.getUsername() != null ? userRo.getUsername() : null);
        this.setEmail(userRo.getEmail() != null ? userRo.getEmail() : null);
        // Hash the passwords
        this.setPassword(userRo.getPassword() != null ? utilService.hashWithPBKDF2(userRo.getPassword(), salt, iterations, keyLength) : null);
        this.setNewPassword(userRo.getNewPassword() != null ? utilService.hashWithPBKDF2(userRo.getNewPassword(), salt, iterations, keyLength) : null);
    }

}

