package com.personal.pdfsummarizer.user.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserPasswordRequest {
    private String userID;
    private String password;
}
