package com.personal.pdfsummarizer.user.constants;

public enum UserError {
    USER_EXISTS(400, "USER_EXISTS", "User already exists");

    private final Integer code;
    private final String businessCode;
    private final String description;

    UserError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public String getDescription() {
        return description;
    }
}
