package com.bbidoleMarket.bbidoleMarket.api.login.controller;

import com.bbidoleMarket.bbidoleMarket.api.login.dto.LoginReqDto;
import com.bbidoleMarket.bbidoleMarket.api.login.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
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

import static com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus.SEND_LOGIN_SUCCESS;

@Tag(name = "로그인", description = "로그인 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(@Valid @RequestBody LoginReqDto loginReqDto) {
        User user = userService.login(loginReqDto);
        return ApiResponse.success(SEND_LOGIN_SUCCESS, user);
    }
}
