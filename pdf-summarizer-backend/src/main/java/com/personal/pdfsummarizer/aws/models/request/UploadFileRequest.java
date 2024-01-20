package com.personal.pdfsummarizer.aws.models.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UploadFileRequest {
    private Flux<ByteBuffer> file;
}
