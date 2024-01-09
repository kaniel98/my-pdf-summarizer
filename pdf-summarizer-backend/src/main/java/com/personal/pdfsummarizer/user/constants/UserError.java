package com.personal.pdfsummarizer.user.constants;

import lombok.Getter;

@Getter
public enum UserError {
    USER_EXISTS(400, "USER_EXISTS", "Another user with this email already exists"),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "User not found");

    private final Integer code;
    private final String businessCode;
    private final String description;

    UserError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
