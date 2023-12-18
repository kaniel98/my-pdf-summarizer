package com.personal.pdfsummarizer.summarizer.service;


import com.personal.pdfsummarizer.user.UserRepository;
import com.personal.pdfsummarizer.user.models.request.CreateUserRequest;
import com.personal.pdfsummarizer.user.models.response.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<CreateUserResponse> createUser(CreateUserRequest user) {
        return null;
    }
}
