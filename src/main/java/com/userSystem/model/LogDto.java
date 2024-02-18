package com.userSystem.model;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

@Data
public class LogDto {
    private String account;
    private String requestApi;
    private int httpCode;
    private String message;


    public LogDto(UserRo userRo, HttpServletRequest request, int httpCode, String message) {
        this.account = userRo.getAccount();
        this.requestApi = request.getRequestURI();
        this.httpCode = httpCode;
        this.message = message;
    }
}
