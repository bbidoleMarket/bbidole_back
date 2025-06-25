package com.bbidoleMarket.bbidoleMarket.api.signup.controller;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
import com.bbidoleMarket.bbidoleMarket.api.signup.dto.SignupReqDto;
import com.bbidoleMarket.bbidoleMarket.common.exception.BaseException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus.ALREADY_REGISTER_EMAIL_EXCEPTION;
import static com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus.ALREADY_REGISTER_NICKNAME_EXCEPTION;
import static com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus.*;

@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "회원가입", description = "회원가입 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SignupController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 사용 중인 이메일")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupReqDto signupReqDto) {
        try {
            userService.signup(signupReqDto);
            return ApiResponse.success_only(SEND_REGISTER_SUCCESS);

        } catch (BaseException e) {
            throw new BaseException(ALREADY_REGISTER_EMAIL_EXCEPTION.getHttpStatus(), 
                                   ALREADY_REGISTER_EMAIL_EXCEPTION.getMessage());
        }
    }
} 