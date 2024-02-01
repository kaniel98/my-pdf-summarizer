package com.personal.pdfsummarizer.external.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExternalSummaryRequest {
    private String prompt = "Create a summary based on the following metadata and content: \n\n";
    private String metadata;
}
