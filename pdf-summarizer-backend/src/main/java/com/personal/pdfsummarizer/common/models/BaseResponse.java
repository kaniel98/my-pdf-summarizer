package com.personal.pdfsummarizer.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BaseResponse<T> {
    private T data;
    private OffsetDateTime timestamp;
}
