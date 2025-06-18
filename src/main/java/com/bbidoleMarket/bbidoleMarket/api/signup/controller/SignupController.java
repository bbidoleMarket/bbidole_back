package com.bbidoleMarket.bbidoleMarket.api.signup.controller;

import com.bbidoleMarket.bbidoleMarket.api.login.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
import com.bbidoleMarket.bbidoleMarket.api.signup.dto.SignupReqDto;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus.SEND_REGISTER_SUCCESS;

@Tag(name = "회원가입", description = "회원가입 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SignupController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(@Valid @RequestBody SignupReqDto signupReqDto) {
        User user = userService.signup(signupReqDto);
        return ApiResponse.success(SEND_REGISTER_SUCCESS, user);
    }
} 