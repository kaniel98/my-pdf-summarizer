package com.personal.pdfsummarizer.aws.service.impl;

import com.personal.pdfsummarizer.aws.config.S3ClientConfigurationProperties;
import com.personal.pdfsummarizer.aws.constants.AWSError;
import com.personal.pdfsummarizer.aws.constants.S3Actions;
import com.personal.pdfsummarizer.aws.models.response.UploadFileResponse;
import com.personal.pdfsummarizer.aws.service.AWSS3Service;
import com.personal.pdfsummarizer.common.CommonUtils;
import com.personal.pdfsummarizer.common.models.BaseException;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class AWSS3ServiceImpl implements AWSS3Service {

    private final S3AsyncClient s3AsyncClient;
    private final S3ClientConfigurationProperties s3Config;

    // * Download file method
    @Override
    public Mono<ResponseEntity<Flux<ByteBuffer>>> downloadFile(String fileKey) {
        // Construct a get object request - Get the requested file
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3Config.getBucketName())
                .key(fileKey)
                .build();

        // Return a Mono that asynchronously publishes the response content as a stream of ByteBuffer
        /* Mono.fromFuture() - Converts a Future into a Mono
         * Constructs a mono that will produce a single value extracted from the given future
         * CompletableFuture is subscribed internally - Mono emits the value produced by the future
         */
        return Mono.fromFuture(s3AsyncClient.getObject(request, AsyncResponseTransformer.toPublisher()))
                .map(resp -> {
                    GetObjectResponse getObjectResponse = resp.response();
                    checkResponse(getObjectResponse, S3Actions.DOWNLOAD_FILE);
                    String filename = getMetadataItem(getObjectResponse, "filename", fileKey);
                    log.info("Retrieved file: filename={}, length={}", filename, getObjectResponse
                            .contentLength());
                    return BaseResponse.successResponse(Flux.from(resp), getObjectResponse.contentType(), filename, String.valueOf(getObjectResponse.contentLength()));
                }).onErrorResume(error -> {
                            log.error("Error downloading file from S3", error);
                            return Mono.error(CommonUtils.baseExceptionHandler(error));
                        }
                );
    }

    // * Upload a single file method
    @Override
    public Flux<ResponseEntity<BaseResponse<UploadFileResponse>>> uploadFile(Flux<ByteBuffer> file, String fileName) {

        // Constructing required parameters for the upload request
        String fileKey = UUID.randomUUID() + "-" + fileName;
        Map<String, String> metadata = new HashMap<>();

        return file.flatMap(fileBuffer -> {
                    long length = fileBuffer.remaining();
                    CompletableFuture<PutObjectResponse> uploadResponse = s3AsyncClient
                            .putObject(PutObjectRequest.builder()
                                            .bucket(s3Config.getBucketName())
                                            .contentLength(length)
                                            .key(fileKey)
                                            .contentType(MediaType.APPLICATION_PDF.toString())
                                            .metadata(metadata)
                                            .build(),
                                    AsyncRequestBody.fromPublisher(file));

                    return Mono.fromFuture(uploadResponse).map(response -> {
                        checkResponse(response, S3Actions.UPLOAD_FILE);
                        return response;
                    });
                })
                // Proceed to check the response and return the key if successful
                .map(responseMono -> {
                    log.info("Uploaded file: key={}", fileKey);
                    return BaseResponse.successResponse(UploadFileResponse.builder().key(fileKey).build());
                }).onErrorResume(error -> {
                    log.error("Error uploading file to S3", error);
                    return Mono.error(CommonUtils.baseExceptionHandler(error));
                });
    }

    // Helper function to check the headers before uploading the file
    private String checkContentType(ByteBuffer file) {
        return "";
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
        // Exception is thrown based on the S3 action
        log.error("Failed call to S3 for {}", action);
        switch (action) {
            case DOWNLOAD_FILE ->
                    throw new BaseException(AWSError.DOWNLOAD_FILE_ERROR.getCode(), AWSError.DOWNLOAD_FILE_ERROR.getBusinessCode(), AWSError.DOWNLOAD_FILE_ERROR.getDescription());
            case UPLOAD_FILE ->
                    throw new BaseException(AWSError.UPLOAD_FILE_ERROR.getCode(), AWSError.UPLOAD_FILE_ERROR.getBusinessCode(), AWSError.UPLOAD_FILE_ERROR.getDescription());
            case DELETE_FILE ->
                    throw new BaseException(AWSError.DELETE_FILE_ERROR.getCode(), AWSError.DELETE_FILE_ERROR.getBusinessCode(), AWSError.DELETE_FILE_ERROR.getDescription());
            default ->
                    throw new BaseException(AWSError.UNEXPECTED_AWS_ERROR.getCode(), AWSError.UNEXPECTED_AWS_ERROR.getBusinessCode(), AWSError.UNEXPECTED_AWS_ERROR.getDescription());
        }
    }
}
