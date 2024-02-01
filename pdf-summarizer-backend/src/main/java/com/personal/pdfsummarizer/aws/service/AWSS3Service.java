package com.personal.pdfsummarizer.aws.service;

import com.personal.pdfsummarizer.aws.models.response.UploadFileResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

public interface AWSS3Service {
    // Unable to use custom BaseResponse class due to it interfering with downloadFile method
    Mono<ResponseEntity<Flux<ByteBuffer>>> downloadFile(String key);

    // * Upload a single file method
    Mono<UploadFileResponse> uploadFile(Flux<ByteBuffer> file, String fileName, String contentType);
}
