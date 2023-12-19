package com.personal.pdfsummarizer.user;

import com.personal.pdfsummarizer.common.constants.APIConstants;
import com.personal.pdfsummarizer.common.models.BaseRequest;
import com.personal.pdfsummarizer.common.models.BaseResponse;
import com.personal.pdfsummarizer.user.models.request.CreateUserRequest;
import com.personal.pdfsummarizer.user.models.response.CreateUserResponse;
import com.personal.pdfsummarizer.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Mono<ResponseEntity<BaseResponse<CreateUserResponse>>> createUser(@RequestBody @Valid BaseRequest<CreateUserRequest> request) {
        return userService.createUser(request.getData())
                .map(BaseResponse::successResponse);
    }
}
