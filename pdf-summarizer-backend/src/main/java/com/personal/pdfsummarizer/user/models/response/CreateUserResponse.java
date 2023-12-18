package com.personal.pdfsummarizer.user.models.response;

import com.personal.pdfsummarizer.user.models.UserBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateUserResponse {
    private UserBase user;
}
