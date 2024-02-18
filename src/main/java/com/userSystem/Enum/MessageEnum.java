package com.userSystem.Enum;

// MessageEnum.java
public enum MessageEnum {
    ACCOUNT_PASSWORD_ERROR("account or password error"),
    LOGIN_SUCCESS("login Success"),
    UPDATE_SUCCESS("update Success"),
    BAD_REQUEST("Bad Request"),
    PASSWORD_CONFIRM_PASSWORD_NOT_THE_SAME("password and confirmPassword must be the same"),
    CONFIRM_PASSWORD_ERROR("password and confirmPassword must be the same"),
    INVALID_JWT_TOKEN("Invalid JWT token");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

