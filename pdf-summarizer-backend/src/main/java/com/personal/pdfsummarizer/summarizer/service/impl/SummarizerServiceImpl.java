package com.personal.pdfsummarizer.summarizer.service.impl;

import com.personal.pdfsummarizer.aws.constants.AWSError;
import com.personal.pdfsummarizer.aws.models.request.PdfDownloadRequest;
import com.personal.pdfsummarizer.aws.service.AWSS3Service;
import com.personal.pdfsummarizer.common.CommonUtils;
import com.personal.pdfsummarizer.common.models.BaseException;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import com.personal.pdfsummarizer.summarizer.constants.SummarizerError;
import com.personal.pdfsummarizer.summarizer.models.request.GenerateSummaryRequest;
import com.personal.pdfsummarizer.summarizer.models.response.GenerateSummaryResponse;
import com.personal.pdfsummarizer.summarizer.service.SummarizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SummarizerServiceImpl implements SummarizerService {

    private final AWSS3Service s3Service;
    private static final HashSet<String> allowedContentTypes = new HashSet<>(List.of(MediaType.APPLICATION_PDF_VALUE));
    private final Tika tika = new Tika();

    @Override
    public Mono<ResponseEntity<Flux<ByteBuffer>>> getSelectedUserPdfFile(PdfDownloadRequest request) {
        // Proposed flow:
        // 1. Check if PDF belongs to user
        // 2. If yes, download PDF
        // 3. If no, return error
        return s3Service.downloadFile(request.getKey());
    }

    @Override
    public Mono<ResponseEntity<BaseResponse<GenerateSummaryResponse>>> generatePdfSummary(HttpHeaders headers, GenerateSummaryRequest request, @RequestParam Flux<ByteBuffer> file) {
        // 1. Check content type to see if it is supported
        String contentType = getContentType(file).block(); // Throw exception if not supported
        // 2. Proceed to extract text from PDF
        Mono<String> extractedText = extractTextFromPdf(file);
        // 3. Craft the request to gemini api and send it to them to generate summary
        // 4. Asynchronously upload the pdf to S3 & Save the summary to data base
        // 5. Return the summary to the user

        return extractedText.map(resp -> {
            GenerateSummaryResponse summaryResponse = GenerateSummaryResponse.builder()
                    .summary(resp)
                    .build();
            return BaseResponse.successResponse(summaryResponse);
        });


//        return s3Service.uploadFile(file, request.getFileName(), contentType).map(resp -> {
//            GenerateSummaryResponse summaryResponse = GenerateSummaryResponse.builder()
//                    .summary(resp.getKey())
//                    .build();
//            return BaseResponse.successResponse(summaryResponse);
//        });
    }

    @Override
    public Mono<String> extractTextFromPdf(Flux<ByteBuffer> file) {
        return file.collectList() // Gather buffers into a list
                .flatMap(buffers -> {
                    byte[] fileBytes = concatBuffers(buffers);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
                    try {
                        return Mono.just(tika.parseToString(inputStream));
                    } catch (IOException | TikaException e) {
                        throw new BaseException(SummarizerError.READING_FILE_ERROR.getCode(), SummarizerError.READING_FILE_ERROR.getBusinessCode(), SummarizerError.READING_FILE_ERROR.getDescription());
                    }
                }).onErrorResume(e -> {
                    log.error("Error extracting text from PDF", e);
                    return Mono.error(CommonUtils.baseExceptionHandler(e));
                });
    }

    // Region: Helper methods to check for file type
    private Mono<String> getContentType(Flux<ByteBuffer> file) {
        // Check the content type of the file
        return file
                .take(5)
                .concatMap(buffer -> {
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    return Mono.just(checkIsAllowedContentType(List.of(ByteBuffer.wrap(bytes))));
                }).next();
    }

    private String checkIsAllowedContentType(List<ByteBuffer> buffers) {
        byte[] initialBytes = concatBuffers(buffers);
        String contentType = tika.detect(initialBytes);
        // Check if the content type is supported - PDF or DOCX
        if (!allowedContentTypes.contains(contentType)) {
            log.error("Unsupported content type: {}", contentType);
            throw new BaseException(AWSError.UNSUPPORTED_CONTENT_TYPE.getCode(), AWSError.UNSUPPORTED_CONTENT_TYPE.getBusinessCode(), AWSError.UNSUPPORTED_CONTENT_TYPE.getDescription());
        }
        return contentType;
    }

    private byte[] concatBuffers(List<ByteBuffer> buffers) {
        int totalLength = buffers.stream().mapToInt(ByteBuffer::remaining).sum();
        ByteBuffer concatenated = ByteBuffer.allocate(totalLength);
        buffers.forEach(concatenated::put);
        concatenated.flip();
        return concatenated.array();
    }
    // End Region
}
