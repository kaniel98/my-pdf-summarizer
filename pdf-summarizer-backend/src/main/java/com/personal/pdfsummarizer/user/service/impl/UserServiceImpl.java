package com.personal.pdfsummarizer.user.service.impl;


import com.personal.pdfsummarizer.common.CommonUtils;
import com.personal.pdfsummarizer.common.models.BaseException;
import com.personal.pdfsummarizer.user.UserRepository;
import com.personal.pdfsummarizer.user.constants.FieldToUpdate;
import com.personal.pdfsummarizer.user.constants.UserError;
import com.personal.pdfsummarizer.user.models.UserEntity;
import com.personal.pdfsummarizer.user.models.request.CreateUserRequest;
import com.personal.pdfsummarizer.user.models.request.GetUserRequest;
import com.personal.pdfsummarizer.user.models.request.UpdateUserRequest;
import com.personal.pdfsummarizer.user.models.request.UpdateUserSummaryRequest;
import com.personal.pdfsummarizer.user.models.response.UpdateUserSummaryResponse;
import com.personal.pdfsummarizer.user.models.response.UserResponse;
import com.personal.pdfsummarizer.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
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
    private final CommonUtils commonUtils;
    // Default options for findAndModify
    private final static FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

    @Override
    public Mono<UserResponse> createUser(CreateUserRequest userInformation) {
        UserEntity newUser = UserEntity.builder()
                .email(userInformation.getEmail())
                .password(CommonUtils.pwHash(userInformation.getPassword()))
                .summaries(new HashMap<>())
                .build();

        return userRepository.save(newUser)
                .map(user -> mapper.map(user, UserResponse.class))
                .onErrorResume(error -> {
                    log.error("Error while saving user: {}", error.getMessage());
                    if (error instanceof DuplicateKeyException) {
                        return Mono.error(new BaseException(UserError.USER_EXISTS.getCode(), UserError.USER_EXISTS.getBusinessCode(), UserError.USER_EXISTS.getDescription()));
                    }
                    return Mono.error(CommonUtils.baseExceptionHandler(error));
                });
    }

    @Override
    public Mono<UserResponse> getUser(GetUserRequest userInformation) {
        return userRepository.findByEmail(userInformation.getEmail())
                .map(user -> mapper.map(user, UserResponse.class))
                .switchIfEmpty(Mono.error(new BaseException(UserError.USER_NOT_FOUND.getCode(), UserError.USER_NOT_FOUND.getBusinessCode(), UserError.USER_NOT_FOUND.getDescription())))
                .onErrorResume(error -> {
                    log.error("Error while getting user: {}", error.getMessage());
                    return Mono.error(CommonUtils.baseExceptionHandler(error));
                });
    }


    @Override
    public Mono<UserResponse> updateUser(UpdateUserRequest updatedUserInformation) {
        // Setting update field based on the field to update
        String fieldToUpdate = "";
        if (updatedUserInformation.getFieldToUpdate().equals(FieldToUpdate.EMAIL)) {
            fieldToUpdate = "email";
        } else if (updatedUserInformation.getFieldToUpdate().equals(FieldToUpdate.PASSWORD)) {
            fieldToUpdate = "password";
        } else if (updatedUserInformation.getFieldToUpdate().equals(FieldToUpdate.SUMMARY)) {
            fieldToUpdate = "summaries";
        }

        Update update = new Update().set(fieldToUpdate, updatedUserInformation.getUpdateFieldValue());
        Query query = new Query().addCriteria(Criteria.where("userID").is(updatedUserInformation.getUserId()));
        Mono<UserEntity> updatedUser = mongoTemplate.findAndModify(query, update, options, UserEntity.class);
        return updatedUser.map(user -> mapper.map(user, UserResponse.class))
                .onErrorResume(error -> {
                    log.error("Error while updating user email: {}", error.getMessage());
                    if (error instanceof DuplicateKeyException) {
                        return Mono.error(new BaseException(UserError.USER_EXISTS.getCode(), UserError.USER_EXISTS.getBusinessCode(), UserError.USER_EXISTS.getDescription()));
                    }
                    return Mono.error(CommonUtils.baseExceptionHandler(error));
                });
    }

    @Override
    public Mono<UpdateUserSummaryResponse> updateUserSummaries(UpdateUserSummaryRequest updatedUserInformation) {
        return null;
    }
}
