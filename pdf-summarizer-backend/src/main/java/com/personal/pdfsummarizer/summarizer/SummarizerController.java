package com.personal.pdfsummarizer.summarizer;

import com.personal.pdfsummarizer.aws.models.PdfDownloadRequest;
import com.personal.pdfsummarizer.common.constants.APIConstants;
import com.personal.pdfsummarizer.common.models.BaseRequest;
import com.personal.pdfsummarizer.summarizer.service.SummarizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}

