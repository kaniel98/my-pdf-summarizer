package com.personal.pdfsummarizer.summarizer.models.request;

import com.personal.pdfsummarizer.summarizer.models.constants.SummaryType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GenerateSummaryRequest {
    @NotEmpty(message = "File name cannot be empty")
    private String fileName;
    @NotEmpty(message = "Number of pointers should be given")
    private Integer numberOfPointers;
    @NotEmpty(message = "Summary type should be given")
    private SummaryType summaryType;
    private Integer summaryLength; // Optional
    private String additionalInstructions; // Optional
}
