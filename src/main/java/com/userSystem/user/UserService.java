package com.userSystem.user;


import com.userSystem.UtilService;
import com.userSystem.mail.ResponseVo;
import com.userSystem.user.model.UserRo;
import com.userSystem.user.model.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;
    UtilService utilService;

    String salt = "yosolo";
    int iterations = 1000;
    int keyLength = 256; // in bits

    public UserRo getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    public List<UserRo> getAllUsers() {
        return userMapper.selectAllUser();
    }

    /**
     * create from webpage
     *
     * @param userRo com.userSystem.user.model.UserRo
     */
    public ResponseVo createUser(UserRo userRo) {
        if (userRo.getUsername() != null && userRo.getAccount() != null && userRo.getPassword() != null && userRo.getEmail() != null) {
            setHashedPassword(userRo);
            try {
                userMapper.insertUser(userRo);
            } catch (Exception e) {
                if (e.toString().contains("Duplicate") && e.toString().contains("email")) {
                    return ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Duplicate email").build();
                } else if (e.toString().contains("Duplicate") && e.toString().contains("account")) {
                    return ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Duplicate account").build();
                }
            }
        } else {
            return ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Bad Request").build();
        }
        return ResponseVo.builder().httpCode(HttpServletResponse.SC_CREATED).message("created ok ").build();
    }

    /**
     * update from webpage
     *
     * @param userRo UserRo
     */
    public void updateUser(UserRo userRo) {
        setHashedPassword(userRo);
        userMapper.updateUser(userRo);
    }

    public void updateUser(Long id, UserRo userRo) {
        userRo.setId(id);
        userMapper.updateUserById(userRo);
    }

    public void deleteUser(Long id) {
        userMapper.deleteUserById(id);
    }

    public UserVo loginUser(UserRo userRo) {
        setHashedPassword(userRo);
        return userMapper.selectUserByAccountPassword(userRo);
    }

    /**
     * set hashedPassword to userRo.password
     *
     * @param userRo UserRo
     */
    void setHashedPassword(UserRo userRo) {
        if (userRo.getPassword() != null) {
            String hashedPassword = utilService.hashWithPBKDF2(userRo.getPassword(), salt, iterations, keyLength);
            userRo.setPassword(hashedPassword);
        }
        if (userRo.getNewPassword() != null) {
            String hashedNewPassword = utilService.hashWithPBKDF2(userRo.getNewPassword(), salt, iterations, keyLength);
            userRo.setNewPassword(hashedNewPassword);
        }
    }

    /**
     * 忘記密碼
     *
     * @param userRo UserRo
     * @return ResponseVo
     */
    public ResponseVo forgotPassword(UserRo userRo) {
        if (userRo.getAccount() != null && userRo.getEmail() != null) {
            return ResponseVo.builder().httpCode(HttpServletResponse.SC_ACCEPTED).message("Success").build();
        } else {
            return ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Bad Request").build();
        }
    }

}

