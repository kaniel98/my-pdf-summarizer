package com.personal.pdfsummarizer.aws.service;

import com.personal.pdfsummarizer.aws.models.response.UploadFileResponse;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

public interface AWSS3Service {
    // Unable to use custom BaseResponse class due to it interfering with downloadFile method
    Mono<ResponseEntity<Flux<ByteBuffer>>> downloadFile(String key);

    Mono<ResponseEntity<BaseResponse<UploadFileResponse>>> uploadFile(HttpHeaders headers, Flux<ByteBuffer> file);
}
