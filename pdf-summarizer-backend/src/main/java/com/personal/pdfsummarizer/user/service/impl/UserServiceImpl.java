package com.personal.pdfsummarizer.user.service.impl;


import com.personal.pdfsummarizer.common.CommonUtils;
import com.personal.pdfsummarizer.common.models.BaseException;
import com.personal.pdfsummarizer.user.UserRepository;
import com.personal.pdfsummarizer.user.constants.UserError;
import com.personal.pdfsummarizer.user.models.UserEntity;
import com.personal.pdfsummarizer.user.models.request.*;
import com.personal.pdfsummarizer.user.models.response.UpdateUserEmailResponse;
import com.personal.pdfsummarizer.user.models.response.UpdateUserSummaryResponse;
import com.personal.pdfsummarizer.user.models.response.UserResponse;
import com.personal.pdfsummarizer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<UserResponse> createUser(CreateUserRequest userInformation) {
        UserEntity newUser = UserEntity.builder()
                .email(userInformation.getEmail())
                // Todo - Hashing of password
                .password(CommonUtils.pwHash(userInformation.getPassword()))
                .summaries(new HashMap<>())
                .build();

        return userRepository.save(newUser)
                .map(user -> mapper.map(user, UserResponse.class))
                .onErrorResume(error -> {
                    log.error("Error while saving user: {}", error.getMessage());
                    if (error instanceof DuplicateKeyException) {
                        return Mono.error(new BaseException(UserError.USER_EXISTS.getCode(), UserError.USER_EXISTS.getDescription()));
                    }
                    return Mono.error(CommonUtils.baseExceptionHandler(error));
                });
    }

    @Override
    public Mono<UserResponse> getUser(GetUserRequest userInformation) {
        return userRepository.findByEmail(userInformation.getEmail())
                .map(user -> mapper.map(user, UserResponse.class))
                .switchIfEmpty(Mono.error(new BaseException(UserError.USER_NOT_FOUND.getCode(), UserError.USER_NOT_FOUND.getDescription())))
                .onErrorResume(error -> {
                    log.error("Error while getting user: {}", error.getMessage());
                    return Mono.error(CommonUtils.baseExceptionHandler(error));
                });
    }

    @Override
    public Mono<UpdateUserEmailResponse> updateUserEmail(UpdateUserEmailRequest updatedUserInformation) {
        Query query = new Query().addCriteria(Criteria.where("userID").is(updatedUserInformation.getUserID()));
        Update update = new Update().set("email", updatedUserInformation.getEmail());
        Mono<UserEntity> updatedUser = mongoTemplate.findAndModify(query, update, UserEntity.class);
        return updatedUser.map(user -> mapper.map(user, UpdateUserEmailResponse.class))
                .onErrorResume(error -> {
                    log.error("Error while updating user email: {}", error.getMessage());
                    return Mono.error(CommonUtils.baseExceptionHandler(error));
                });
    }

    @Override
    public Mono<UpdateUserPasswordRequest> updateUserPassword(UpdateUserPasswordRequest updatedUserInformation) {
        return null;
    }

    @Override
    public Mono<UpdateUserSummaryRequest> updateUser(UpdateUserSummaryResponse updatedUserInformation) {
        return null;
    }

}
