package com.userSystem.model;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class LogDto {
    private String account;
    private String requestApi;
    private int httpCode;
    private String message;

    public LogDto(UserRo userRo, HttpServletRequest request , ResponseVo responseVo) {
        this.account = userRo.getAccount();
        this.requestApi = request.getRequestURI().toString();
        this.httpCode =responseVo.getHttpCode();
        this.message = responseVo.getMessage();
    }
}
