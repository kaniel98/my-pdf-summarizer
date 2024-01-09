package com.personal.pdfsummarizer.user.service;

import com.personal.pdfsummarizer.user.models.request.CreateUserRequest;
import com.personal.pdfsummarizer.user.models.request.GetUserRequest;
import com.personal.pdfsummarizer.user.models.request.UpdateUserRequest;
import com.personal.pdfsummarizer.user.models.request.UpdateUserSummaryRequest;
import com.personal.pdfsummarizer.user.models.response.UpdateUserSummaryResponse;
import com.personal.pdfsummarizer.user.models.response.UserResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponse> createUser(CreateUserRequest user);

    Mono<UserResponse> getUser(GetUserRequest userInformation);

    // * Only used for updating user's base information (email, password)
    Mono<UserResponse> updateUser(UpdateUserRequest updatedUserInformation);

    // * Only used for updating user's summaries - E.g., adding a new summary
    Mono<UpdateUserSummaryResponse> updateUserSummaries(UpdateUserSummaryRequest updatedUserInformation);
}
