package com.personal.pdfsummarizer.summarizer;

import com.personal.pdfsummarizer.aws.models.request.PdfDownloadRequest;
import com.personal.pdfsummarizer.common.constants.APIConstants;
import com.personal.pdfsummarizer.common.models.BaseRequest;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import com.personal.pdfsummarizer.summarizer.models.request.GenerateSummaryRequest;
import com.personal.pdfsummarizer.summarizer.models.response.GenerateSummaryResponse;
import com.personal.pdfsummarizer.summarizer.service.SummarizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/summarizer")
public class SummarizerController {
    private final SummarizerService summarizerService;

    // * API 2.1 Get a selected user's pdf file
    @PostMapping("/get")
    @Operation(summary = "Download a PDF file from a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF downloaded successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public Mono<ResponseEntity<Flux<ByteBuffer>>> getSelectedUserPdfFile(@RequestBody @Valid BaseRequest<PdfDownloadRequest> request) {
        return summarizerService.getSelectedUserPdfFile(request.getData());
    }

    // * API 2.2 Generate a summary of a PDF file (Currently upload file only)
    @PostMapping("/generate")
    @Operation(summary = "Generate a summary of a PDF/DOCX file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary generated successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public Mono<ResponseEntity<BaseResponse<GenerateSummaryResponse>>> generatePdfSummary(@RequestHeader HttpHeaders headers, @ModelAttribute GenerateSummaryRequest request, @RequestPart Flux<ByteBuffer> file) {
        return summarizerService.generatePdfSummary(headers, request, file);
    }
}

