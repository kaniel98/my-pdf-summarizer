package com.personal.pdfsummarizer.summarizer.constants;

import lombok.Getter;

@Getter
public enum SummarizerError {
    READING_FILE_ERROR(400, "READING_FILE_ERROR", "Error while reading file");

    private final Integer code;
    private final String businessCode;
    private final String description;

    SummarizerError(Integer code, String businessCode, String description) {
        this.code = code;
        this.businessCode = businessCode;
        this.description = description;
    }
}
