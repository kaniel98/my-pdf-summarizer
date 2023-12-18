package com.personal.pdfsummarizer.summarizer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Prompt {
    private int numberOfPoints;
    private int numberOfSentencesPerPoint;
}
