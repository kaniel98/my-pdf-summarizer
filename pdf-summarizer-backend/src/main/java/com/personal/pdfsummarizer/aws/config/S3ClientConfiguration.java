package com.personal.pdfsummarizer.aws.config;

import io.micrometer.common.util.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
// Spring annotation that enables bean creation and property binding for the specified configuration properties class
// Ensures properties defined in configuration files e.g., Application.yml are bound to the S3ClientConfigurationProperties class
// Makes it accessible to the application - Inject it where needed
@EnableConfigurationProperties(S3ClientConfigurationProperties.class)
public class S3ClientConfiguration { // Used to create S3AsyncClient bean - Start of API calls to AWS S3
    //  * Creates an S3AsyncClient bean
    //  * @param s3props S3ClientConfigurationProperties object -  Holds configuration properties for S3, likely loaded from application.yml.
    //  * @param credentialsProvider AwsCredentialsProvider object - Provides AWS credentials for authentication.
    @Bean
    public S3AsyncClient s3client(S3ClientConfigurationProperties s3props, AwsCredentialsProvider credentialsProvider) {
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .maxConcurrency(64) // Maximum number of concurrent request the client will ever make at a time
                .build();

        S3Configuration serviceConfiguration = S3Configuration.builder()
                .checksumValidationEnabled(true) // Disables checksum validation for all requests
                .chunkedEncodingEnabled(true) // Enables chunked encoding for all requests - Allows for streaming uploads
                .build();

        S3AsyncClientBuilder s3AsyncClientBuilder = S3AsyncClient.builder()
                .httpClient(httpClient) // Sets the HTTP client to use for sending requests to AWS
                .region(s3props.getRegion()) // Sets the AWS region to use when making requests
                .credentialsProvider(credentialsProvider) // Sets the AWS credentials provider to use for authentication
                .serviceConfiguration(serviceConfiguration); // Sets the configuration to use for this service client

        if (s3props.getEndpoint() != null) {
            s3AsyncClientBuilder = s3AsyncClientBuilder.endpointOverride(s3props.getEndpoint());
        }

        return s3AsyncClientBuilder.build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(S3ClientConfigurationProperties s3props) {
        if (StringUtils.isBlank(s3props.getAccessKey())) {
            // Return default provider
            return DefaultCredentialsProvider.create();
        }
        // Return custom credentials provider
        return () -> {
            return (AwsCredentials) AwsBasicCredentials.create(s3props.getAccessKey(), s3props.getSecretKey());
        };
    }
}
