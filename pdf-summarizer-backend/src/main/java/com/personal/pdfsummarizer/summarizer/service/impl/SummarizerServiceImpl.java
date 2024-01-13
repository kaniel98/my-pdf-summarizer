package com.personal.pdfsummarizer.summarizer.service.impl;

import com.personal.pdfsummarizer.aws.models.PdfDownloadRequest;
import com.personal.pdfsummarizer.aws.service.AWSS3Service;
import com.personal.pdfsummarizer.summarizer.service.SummarizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
}
