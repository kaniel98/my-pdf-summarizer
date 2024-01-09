package com.personal.pdfsummarizer.user;

import com.personal.pdfsummarizer.common.constants.APIConstants;
import com.personal.pdfsummarizer.common.models.BaseRequest;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import com.personal.pdfsummarizer.user.models.request.CreateUserRequest;
import com.personal.pdfsummarizer.user.models.request.GetUserRequest;
import com.personal.pdfsummarizer.user.models.request.UpdateUserRequest;
import com.personal.pdfsummarizer.user.models.response.UserResponse;
import com.personal.pdfsummarizer.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    // * API 1.1 Create a new user
    @PostMapping("/new")
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public Mono<ResponseEntity<BaseResponse<UserResponse>>> createUser(@RequestBody @Valid BaseRequest<CreateUserRequest> request) {
        return userService.createUser(request.getData())
                .map(BaseResponse::successResponse);
    }

    // * API 1.2 Get a user
    @PostMapping("/get")
    @Operation(summary = "Get a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public Mono<ResponseEntity<BaseResponse<UserResponse>>> getUser(@RequestBody @Valid BaseRequest<GetUserRequest> request) {
        return userService.getUser(request.getData())
                .map(BaseResponse::successResponse);
    }

    // * API 1.3 Update a user
    @PatchMapping("/update")
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = APIConstants.BAD_REQUEST_MESSAGE),
            @ApiResponse(responseCode = "500", description = APIConstants.INTERNAL_SERVER_ERROR_MESSAGE),
            @ApiResponse(responseCode = "401", description = APIConstants.UNAUTHORIZED_MESSAGE),
            @ApiResponse(responseCode = "404", description = APIConstants.NOT_FOUND_MESSAGE),
    })
    public Mono<ResponseEntity<BaseResponse<UserResponse>>> updateUser(@RequestBody @Valid BaseRequest<UpdateUserRequest> request) {
        return userService.updateUser(request.getData())
                .map(BaseResponse::successResponse);
    }
}
