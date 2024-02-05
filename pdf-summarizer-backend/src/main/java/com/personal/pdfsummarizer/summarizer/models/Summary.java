package com.personal.pdfsummarizer.summarizer.models;

import com.personal.pdfsummarizer.summarizer.models.request.GenerateSummaryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Summary {
    private String pdfID;
    private String pdfName;
    private String pdfPath;
    private String pdfText;
    private GenerateSummaryRequest generateSummaryRequest;
    private String pdfSummary;
}
