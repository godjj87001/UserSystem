package com.userSystem.Service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.userSystem.Enum.MessageEnum;
import com.userSystem.dao.LogMapper;
import com.userSystem.model.*;
import com.userSystem.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
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

    UserService(UserMapper userMapper, UtilService utilService, LogMapper logMapper) {
        this.userMapper = userMapper;
        this.logMapper = logMapper;
        this.utilService = utilService;
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
    public ResponseVo createUser(UserRo userRo, HttpServletRequest request) {
        ResponseVo responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_CREATED).message("created ok ").build();
        LogDto logDto;
        if (userRo.getUsername() != null && userRo.getAccount() != null && userRo.getPassword() != null && userRo.getEmail() != null && userRo.getConfirmPassword() != null) {
            try {
                if (userRo.getPassword() == userRo.getConfirmPassword()) {
                    UserVo userVo = loginUser(userRo);
                    if (userVo.getAccount().equals(userRo.getAccount())) {
                        responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Duplicate account").build();
                    } else if (userVo.getEmail().equals(userRo.getEmail())) {
                        responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Duplicate email").build();
                    } else {
                        UserBo userBo = new UserBo(userRo);
                        userMapper.insertUser(userBo);
                    }
                } else {
                    responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED).message("password and confirmPassword must be the same").build();
                }

            } catch (Exception e) {
                log.error("" + e);
            }
        } else {
            responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Bad Request").build();
        }
        logDto = new LogDto(userRo, request, responseVo);
        logMapper.insertApiLog(logDto);
        return responseVo;
    }

    public UserVo loginUser(UserRo userRo, HttpServletRequest request) {
        UserVo userVo = loginUser(userRo);
        ResponseVo responseVo;
        if (userVo.getAccount() != null) {
            responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_OK).message(MessageEnum.LOGIN_SUCCESS.getMessage()).build();
        } else {
            responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_UNAUTHORIZED).message(MessageEnum.ACCOUNT_PASSWORD_ERROR.getMessage()).build();
        }
        LogDto logDto = new LogDto(userRo, request, responseVo);
        logMapper.insertApiLog(logDto);
        return userVo;
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
    public ResponseVo updateUser(UserRo userRo, HttpServletRequest request) {
        try {
            // 1. 更改密碼與確認密碼是否相同
            if (userRo.getPassword() != null && userRo.getConfirmPassword() != null && userRo.getNewPassword() != userRo.getConfirmPassword()) {
                return ResponseVo.builder().httpCode(HttpServletResponse.SC_METHOD_NOT_ALLOWED).message("password and confirmPassword must be the same").build();
            }
            // 2.檢查是否有User
            UserVo userVo = loginUser(userRo);
            if (userVo != null) {
                // 3.userRo to userBo
                UserBo userBo = new UserBo(userRo);
                // 4. updateUser
                userMapper.updateUser(userBo);
            } else {
                ResponseVo responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_UNAUTHORIZED).message("account or password error").build();
                logMapper.insertApiLog(new LogDto(userRo, request, responseVo));
                return responseVo;
            }
            ResponseVo responseVo = ResponseVo.builder().httpCode(HttpServletResponse.SC_ACCEPTED).message("update Success").build();
            logMapper.insertApiLog(new LogDto(userRo, request, responseVo));
            return responseVo;
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
     *
     * @param userRo UserRo
     * @return ResponseVo
     */
    public ResponseVo forgotPassword(UserRo userRo) {
        if (userRo.getAccount() != null || userRo.getEmail() != null) {
            UserBo userBo = new UserBo(userRo);
            UserVo userVo = userMapper.selectUserByAccountOrEmail(userBo);
            if (userVo.getEmail() != null) {
                String jwtToken = generateJwtToken(userRo.getEmail());
                EmailRO requestEntity = getForgotPasswordRequestEntity(jwtToken, userVo);
                EmailRO emailRO = utilService.sendHttpPostRequest("/email", new HttpEntity<>(requestEntity), EmailRO.class);
            } else {
                ResponseVo.builder().httpCode(HttpServletResponse.SC_ACCEPTED).message(MessageEnum.ACCOUNT_PASSWORD_ERROR.getMessage()).build();
            }
            return ResponseVo.builder().httpCode(HttpServletResponse.SC_ACCEPTED).message("Success").build();
        } else {
            return ResponseVo.builder().httpCode(HttpServletResponse.SC_BAD_REQUEST).message("Bad Request").build();
        }
    }

    private EmailRO getForgotPasswordRequestEntity(String jwtToken, UserVo userVo) {
        EmailRO emailRO = new EmailRO();
        emailRO.setContent(jwtToken);
        emailRO.setReceiver(userVo.getEmail());
        emailRO.setSubject("忘記密碼");
        return emailRO;
    }

    private String generateJwtToken(String identifier) {
        long EXPIRATION_TIME = 86400000; //24 hours
        // Use your JWT library (e.g., Auth0 Java JWT) to create a token with the user identifier
        // Example using Auth0 Java JWT:
        Algorithm algorithm = Algorithm.HMAC256("forgotPassword");
        return JWT.create()
                .withClaim("email", identifier)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }


}

