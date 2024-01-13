package com.personal.pdfsummarizer.summarizer.service;

import com.personal.pdfsummarizer.aws.models.PdfDownloadRequest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

public interface SummarizerService {
    Mono<ResponseEntity<Flux<ByteBuffer>>> getSelectedUserPdfFile(PdfDownloadRequest request);
}
