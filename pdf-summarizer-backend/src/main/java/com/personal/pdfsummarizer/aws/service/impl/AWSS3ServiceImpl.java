package com.personal.pdfsummarizer.aws.service.impl;

import com.personal.pdfsummarizer.aws.config.S3ClientConfigurationProperties;
import com.personal.pdfsummarizer.aws.constants.AWSError;
import com.personal.pdfsummarizer.aws.constants.S3Actions;
import com.personal.pdfsummarizer.aws.service.AWSS3Service;
import com.personal.pdfsummarizer.common.CommonUtils;
import com.personal.pdfsummarizer.common.models.BaseException;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Response;

import java.nio.ByteBuffer;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class AWSS3ServiceImpl implements AWSS3Service {

    private final S3AsyncClient s3AsyncClient;
    private final S3ClientConfigurationProperties s3Config;

    @Override
    public Mono<ResponseEntity<Flux<ByteBuffer>>> downloadFile(String fileKey) {
        // Construct a get object request - Get the requested file
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3Config.getBucketName())
                .key(fileKey)
                .build();

        // Return a Mono that asynchronously publishes the response content as a stream of ByteBuffer
        return Mono.fromFuture(s3AsyncClient.getObject(request, AsyncResponseTransformer.toPublisher()))
                .map(response -> {
                    GetObjectResponse getObjectResponse = response.response();
                    checkResponse(getObjectResponse, S3Actions.DOWNLOAD_FILE);
                    String filename = getMetadataItem(getObjectResponse, "filename", fileKey);
                    log.info("filename={}, length={}", filename, getObjectResponse
                            .contentLength());
                    return BaseResponse.successResponse(Flux.from(response), getObjectResponse.contentType(), filename, String.valueOf(getObjectResponse.contentLength()));
                }).onErrorResume(error -> {
                            log.error("Error downloading file from S3", error);
                            return Mono.error(CommonUtils.baseExceptionHandler(error));
                        }
                );
    }

    // Helper method to get metadata from the response from the API call to AWS S3
    private String getMetadataItem(GetObjectResponse sdkResponse, String key, String defaultValue) {
        for (Map.Entry<String, String> entry : sdkResponse.metadata()
                .entrySet()) {
            if (entry.getKey()
                    .equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return defaultValue;
    }

    // Helper method to check the response from the API call to AWS S3
    private void checkResponse(S3Response response, S3Actions action) {
        SdkHttpResponse sdkHttpResponse = response.sdkHttpResponse();
        if (sdkHttpResponse != null && sdkHttpResponse.isSuccessful()) {
            log.info("Success call to S3 for {}", action);
            return;
        }
        // If the action is download file, throw a custom exception
        log.error("Failed call to S3 for {}", action);
        if (action.equals(S3Actions.DOWNLOAD_FILE)) {
            throw new BaseException(AWSError.DOWNLOAD_FILE_ERROR.getCode(), AWSError.DOWNLOAD_FILE_ERROR.getBusinessCode(), AWSError.DOWNLOAD_FILE_ERROR.getDescription());
        }
        throw new BaseException(AWSError.UNEXPECTED_AWS_ERROR.getCode(), AWSError.UNEXPECTED_AWS_ERROR.getBusinessCode(), AWSError.UNEXPECTED_AWS_ERROR.getDescription());
    }
}
