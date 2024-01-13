package com.personal.pdfsummarizer.aws.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

@ConfigurationProperties(prefix = "aws.properties.s3") // Used to bind properties from application.yml
@Data
public class S3ClientConfigurationProperties {

    private Region region = Region.AP_SOUTHEAST_1;
    private String bucketName;
    private String accessKey;
    private String secretKey;

    private URI endpoint = null;
    // In case we want to do multipart upload
    // AWS S3 requires that file parts must have at least 5MB, except
    // for the last part. This may change for other S3-compatible services,
    private int multipartMinPartSize = 5 * 1024 * 1024;
}
