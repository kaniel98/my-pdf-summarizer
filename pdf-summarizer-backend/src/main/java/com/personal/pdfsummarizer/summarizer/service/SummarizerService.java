package com.personal.pdfsummarizer.summarizer.service;

import com.personal.pdfsummarizer.aws.models.request.PdfDownloadRequest;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import com.personal.pdfsummarizer.summarizer.models.response.GenerateSummaryResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

public interface SummarizerService {
    Mono<ResponseEntity<Flux<ByteBuffer>>> getSelectedUserPdfFile(PdfDownloadRequest request);

    Mono<ResponseEntity<BaseResponse<GenerateSummaryResponse>>> generatePdfSummary(HttpHeaders headers, Flux<ByteBuffer> file);
}
