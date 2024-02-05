package com.personal.pdfsummarizer.external.models.response;

import com.personal.pdfsummarizer.external.models.Output;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExternalSummaryResponse {
    private List<Output> candidates;
}
