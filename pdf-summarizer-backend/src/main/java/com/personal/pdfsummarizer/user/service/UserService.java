package com.personal.pdfsummarizer.user.service;

import com.personal.pdfsummarizer.user.models.request.CreateUserRequest;
import com.personal.pdfsummarizer.user.models.response.CreateUserResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<CreateUserResponse> createUser(CreateUserRequest user);
}
