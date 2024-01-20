package com.personal.pdfsummarizer.summarizer.service.impl;

import com.personal.pdfsummarizer.aws.models.request.PdfDownloadRequest;
import com.personal.pdfsummarizer.aws.service.AWSS3Service;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import com.personal.pdfsummarizer.summarizer.models.response.GenerateSummaryResponse;
import com.personal.pdfsummarizer.summarizer.service.SummarizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
@Service
@Slf4j
public class SummarizerServiceImpl implements SummarizerService {

    private final AWSS3Service s3Service;

    @Override
    public Mono<ResponseEntity<Flux<ByteBuffer>>> getSelectedUserPdfFile(PdfDownloadRequest request) {
        // Proposed flow:
        // 1. Check if PDF belongs to user
        // 2. If yes, download PDF
        // 3. If no, return error
        return s3Service.downloadFile(request.getKey());
    }

    @Override
    public Mono<ResponseEntity<BaseResponse<GenerateSummaryResponse>>> generatePdfSummary(HttpHeaders headers, @RequestParam("file") Flux<ByteBuffer> file) {
        return s3Service.uploadFile(headers, file).map(resp -> {
            GenerateSummaryResponse summaryResponse = GenerateSummaryResponse.builder()
                    .summary(resp.getBody().getData().getKey())
                    .build();
            return BaseResponse.successResponse(summaryResponse);
        });
    }
}
