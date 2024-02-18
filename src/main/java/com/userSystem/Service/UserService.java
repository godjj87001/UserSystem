package com.userSystem.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.userSystem.Enum.MessageEnum;
import com.userSystem.dao.LogMapper;
import com.userSystem.model.*;
import com.userSystem.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private UserMapper userMapper;
    private LogMapper logMapper;
    private UtilService utilService;
    private JwtService jwtService;

    @Value("${email_post_url}")
    private String emailPostUrl;

    UserService(UserMapper userMapper, UtilService utilService, LogMapper logMapper ,JwtService jwtService) {
        this.userMapper = userMapper;
        this.logMapper = logMapper;
        this.utilService = utilService;
        this.jwtService = jwtService;
    }


    public UserRo getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    public List<UserRo> getAllUsers() {
        return userMapper.selectAllUser();
    }

    /**
     * create from webpage
     *
     * @param userRo  com.userSystem.user.model.UserRo
     * @param request
     */
    public ResponseEntity<?> createUser(UserRo userRo, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        String message = "created ok";

        LogDto logDto;

        if (userRo.getUsername() != null && userRo.getAccount() != null && userRo.getPassword() != null && userRo.getEmail() != null && userRo.getConfirmPassword() != null) {
            try {
                if (userRo.getPassword().equals(userRo.getConfirmPassword())) {
                    UserVo userVo = userMapper.selectUserByAccountOrEmail(new UserBo(userRo));
                    if (userVo != null && userVo.getAccount().equals(userRo.getAccount())) {
                        httpStatus = HttpStatus.BAD_REQUEST;
                        message = "Duplicate account";
                    } else if (userVo != null && userVo.getEmail().equals(userRo.getEmail())) {
                        httpStatus = HttpStatus.BAD_REQUEST;
                        message = "Duplicate email";
                    } else {
                        UserBo userBo = new UserBo(userRo);
                        userMapper.insertUser(userBo);
                    }
                } else {
                    httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
                    message = "password and confirmPassword must be the same";
                }
            } catch (Exception e) {
                log.error("" + e);
            }
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Bad Request";
        }

        ResponseEntity<String> responseEntity = new ResponseEntity<>(message, httpStatus);

        logDto = new LogDto(userRo, request, httpStatus.value(), message);
        logMapper.insertApiLog(logDto);

        return responseEntity;
    }


    public ResponseEntity<UserVo> loginUser(UserRo userRo, HttpServletRequest request) {
        HttpStatus httpStatus;
        String message;
        UserVo userVo = null;
        try {
            userVo = loginUser(userRo);
            if (userVo.getAccount() != null) {
                httpStatus = HttpStatus.OK;
                message = MessageEnum.LOGIN_SUCCESS.getMessage();
            } else {
                httpStatus = HttpStatus.UNAUTHORIZED;
                message = MessageEnum.ACCOUNT_PASSWORD_ERROR.getMessage();
            }
            LogDto logDto = new LogDto(userRo, request, httpStatus.value(), message);
            logMapper.insertApiLog(logDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(userVo);
    }

    public UserVo loginUser(UserRo userRo) {
        UserBo userBo = new UserBo(userRo);
        UserVo userVo = userMapper.selectUserByAccountPassword(userBo);
        return userVo;
    }

    /**
     * update from webpage
     * 1. 更改密碼與確認密碼是否相同
     * 2.檢查是否有User
     * 3.userRo to userBo
     * 4. updateUser
     *
     * @param userRo  UserRo
     * @param request
     */
    public ResponseEntity<?> updateUser(UserRo userRo, HttpServletRequest request) {
        HttpStatus httpStatus;
        String message;
        ResponseEntity responseEntity;
        try {
            // 1. 更改密碼與確認密碼是否相同
            if (userRo.getPassword() != null && userRo.getConfirmPassword() != null && userRo.getNewPassword() != userRo.getConfirmPassword()) {
                httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
                message = MessageEnum.PASSWORD_CONFIRM_PASSWORD_NOT_THE_SAME.getMessage();
                logMapper.insertApiLog(new LogDto(userRo, request, httpStatus.value(), message));
                return ResponseEntity.status(httpStatus).body(message);
            }
            // 2.檢查是否有User
            UserVo userVo = loginUser(userRo);
            if (userVo != null) {
                // 3.userRo to userBo
                UserBo userBo = new UserBo(userRo);
                // 4. updateUser
                userMapper.updateUser(userBo);
            } else {
                httpStatus = HttpStatus.UNAUTHORIZED;
                message = MessageEnum.ACCOUNT_PASSWORD_ERROR.getMessage();
                logMapper.insertApiLog(new LogDto(userRo, request, httpStatus.value(), message));
                return ResponseEntity.status(httpStatus).body(message);
            }
            ResponseVo responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_ACCEPTED).message("update Success").build();
            httpStatus = HttpStatus.ACCEPTED;
            message = MessageEnum.UPDATE_SUCCESS.getMessage();
            logMapper.insertApiLog(new LogDto(userRo, request, httpStatus.value(), message));
            return ResponseEntity.status(httpStatus).body(message);
        } catch (Exception e) {
            log.error("" + e);
            throw new RuntimeException(e);
        }
    }

    public void updateUser(Long id, UserRo userRo) {
        userRo.setId(id);
        userMapper.updateUserById(userRo);
    }

    public void deleteUser(Long id) {
        userMapper.deleteUserById(id);
    }

    /**
     * 忘記密碼
     * 1. 檢查參數是否為空 ，是則回傳
     * 2. userRo to Bo
     * 3. select db
     * 4. 如果email為空 回傳登入失敗
     *
     * @param userRo UserRo
     * @return ResponseVo
     */
    public ResponseEntity<?> forgotPassword(UserRo userRo) {
        // 1.檢查參數是否為空
        if (userRo.getAccount() == null && userRo.getEmail() == null) {
            return ResponseEntity.badRequest().body(MessageEnum.BAD_REQUEST.getMessage());
        }
        // 2. userRo to Bo
        UserBo userBo = new UserBo(userRo);
        // 3. select db
        UserVo userVo = userMapper.selectUserByAccountOrEmail(userBo);
        // 4. 如果email為空 回傳登入失敗
        if (userVo.getEmail() == null) {
            return ResponseEntity.badRequest().body(MessageEnum.ACCOUNT_PASSWORD_ERROR.getMessage());
        }
        // 5. 生成 JWT token
        String jwtToken = jwtService.generateJwtToken("email", userVo.getEmail());
        EmailRO requestEntity = getForgotPasswordRequestEntity(jwtToken, userVo);
        EmailRO emailRO = utilService.sendHttpPostRequest(emailPostUrl, new HttpEntity<>(requestEntity), EmailRO.class);

        return ResponseEntity.accepted().build();
    }


    private EmailRO getForgotPasswordRequestEntity(String jwtToken, UserVo userVo) {
        EmailRO emailRO = new EmailRO();
        emailRO.setContent(jwtToken);
        emailRO.setReceiver(userVo.getEmail());
        emailRO.setSubject("忘記密碼");
        return emailRO;
    }




    public ResponseEntity<?> changePassword(UserRo userRo, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        String message = "";

        if (userRo.getAccessToken() != null) {
            // 這裡檢查並使用JWT Token
            DecodedJWT decodedJWT = jwtService.verifyJwt(userRo.getAccessToken());
            if (decodedJWT != null && userRo.getNewPassword() != null && userRo.getConfirmPassword() != null) {
                // JWT 驗證成功，繼續處理
                String email = decodedJWT.getClaim("email").asString();
                String newPassword = userRo.getNewPassword();
                String confirmPassword = userRo.getConfirmPassword();
                // 驗證新密碼和確認密碼是否匹配，進行相應的業務邏輯
                if (newPassword.equals(confirmPassword)) {
                    userMapper.updatePassword(new UserBo(userRo),email);
                }

                // 假設業務邏輯成功
                return ResponseEntity.ok("Password changed successfully for user: " + email);
            } else {
                // JWT 驗證失敗
                httpStatus = HttpStatus.UNAUTHORIZED;
                message = MessageEnum.INVALID_JWT_TOKEN.getMessage();
                return ResponseEntity.badRequest().body("Invalid JWT token");
            }

        } else {
            return ResponseEntity.status(httpStatus).body(message);
        }
    }

    public ResponseEntity<String> getJwt (){
        String jwtToken = jwtService.generateJwtToken("email", "godjj87001@gmail.com");
        return ResponseEntity.ok(jwtToken);
    }

}

