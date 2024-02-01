package com.personal.pdfsummarizer.aws.constants;

import lombok.Getter;

@Getter
public enum AWSError {
    DOWNLOAD_FILE_ERROR(500, "DOWNLOAD_FILE_ERROR", "Error while downloading file"),
    UPLOAD_FILE_ERROR(500, "UPLOAD_FILE_ERROR", "Error while uploading file"),
    DELETE_FILE_ERROR(500, "DELETE_FILE_ERROR", "Error while deleting file"),
    FILE_NOT_FOUND(404, "FILE_NOT_FOUND", "File not found"),
    UNEXPECTED_AWS_ERROR(500, "UNEXPECTED_ERROR", "Unexpected error"),
    UNSUPPORTED_CONTENT_TYPE(400, "UNSUPPORTED_CONTENT_TYPE", "Unsupported content type");

    private final Integer code;
    private final String businessCode;
    private final String description;

    AWSError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
