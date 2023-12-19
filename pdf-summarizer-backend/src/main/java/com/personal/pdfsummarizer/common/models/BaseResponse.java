package com.personal.pdfsummarizer.common.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BaseResponse<T> {
    private boolean success;
    private T data;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public static <T> ResponseEntity<BaseResponse<T>> successResponse(T data) {
        return ResponseEntity.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BaseResponse.<T>builder()
                        .success(true)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    public static <T> ResponseEntity<BaseResponse<T>> failureResponse(T data) {
        return ResponseEntity.status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BaseResponse.<T>builder()
                        .success(false)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
