package com.personal.pdfsummarizer.user.service;


import com.personal.pdfsummarizer.common.constants.CommonError;
import com.personal.pdfsummarizer.common.models.BaseException;
import com.personal.pdfsummarizer.user.UserRepository;
import com.personal.pdfsummarizer.user.constants.UserError;
import com.personal.pdfsummarizer.user.models.UserEntity;
import com.personal.pdfsummarizer.user.models.request.CreateUserRequest;
import com.personal.pdfsummarizer.user.models.response.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Mono<CreateUserResponse> createUser(CreateUserRequest userInformation) {
        UserEntity newUser = UserEntity.builder()
                .email(userInformation.getEmail())
                .password(userInformation.getPassword())
                .summaries(new HashMap<>())
                .build();

        return userRepository.save(newUser)
                .map(user -> mapper.map(user, CreateUserResponse.class))
                .onErrorResume(error -> {
                    log.error("Error while saving user: {}", error.getMessage());
                    if (error instanceof DuplicateKeyException) {
                        return Mono.error(new BaseException(UserError.USER_EXISTS.getCode(), UserError.USER_EXISTS.getDescription()));
                    }
                    return Mono.error(new BaseException(CommonError.INTERNAL_SERVER_ERROR.getCode(), CommonError.INTERNAL_SERVER_ERROR.getDescription()));
                });
    }
}
