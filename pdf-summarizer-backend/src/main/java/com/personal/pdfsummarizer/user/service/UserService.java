package com.personal.pdfsummarizer.user.service;

import com.personal.pdfsummarizer.user.models.request.*;
import com.personal.pdfsummarizer.user.models.response.UpdateUserEmailResponse;
import com.personal.pdfsummarizer.user.models.response.UpdateUserSummaryResponse;
import com.personal.pdfsummarizer.user.models.response.UserResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponse> createUser(CreateUserRequest user);

    Mono<UserResponse> getUser(GetUserRequest userInformation);

    // * Only used for updating user's base information (email, password)
    Mono<UpdateUserEmailResponse> updateUserEmail(UpdateUserEmailRequest updatedUserInformation);

    Mono<UpdateUserPasswordRequest> updateUserPassword(UpdateUserPasswordRequest updatedUserInformation);

    // * Only used for updating user's summaries - E.g., adding a new summary
    Mono<UpdateUserSummaryRequest> updateUser(UpdateUserSummaryResponse updatedUserInformation);
}
