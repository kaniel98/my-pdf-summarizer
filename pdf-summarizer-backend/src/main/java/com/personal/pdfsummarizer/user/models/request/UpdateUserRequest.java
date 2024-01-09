package com.personal.pdfsummarizer.user.models.request;

import com.personal.pdfsummarizer.user.constants.FieldToUpdate;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserRequest {
    @NotEmpty(message = "User ID cannot be empty")
    private String userId;
    @NotEmpty(message = "Field to update cannot be empty")
    private FieldToUpdate fieldToUpdate;
    @NotEmpty(message = "Update field value cannot be empty")
    private String updateFieldValue;
}
