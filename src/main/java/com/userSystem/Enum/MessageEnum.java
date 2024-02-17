package com.userSystem.Enum;

// MessageEnum.java
public enum MessageEnum {
    ACCOUNT_PASSWORD_ERROR("account or password error"),
    LOGIN_SUCCESS("login Success");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

