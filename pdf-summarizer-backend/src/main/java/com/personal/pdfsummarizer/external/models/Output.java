package com.personal.pdfsummarizer.external.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Output {
    private String output;
    private List<Object> safetyRatings;
}
