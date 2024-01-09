package com.personal.pdfsummarizer.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BaseException extends RuntimeException {
    private Integer statusCode;
    private String businessCode;
    private String description;
}
