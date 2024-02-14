package com.userSystem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseVo {
    private int httpCode;
    private String message;
}
