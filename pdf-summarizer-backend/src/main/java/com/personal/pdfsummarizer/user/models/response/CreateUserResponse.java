package com.personal.pdfsummarizer.user.models.response;

import com.personal.pdfsummarizer.summarizer.models.Summary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateUserResponse {
    private String email;
    private HashMap<String, Summary> summaries;
}
