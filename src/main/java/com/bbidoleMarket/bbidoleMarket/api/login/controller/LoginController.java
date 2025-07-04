package com.bbidoleMarket.bbidoleMarket.api.login.controller;

import com.bbidoleMarket.bbidoleMarket.api.entity.Role;
import com.bbidoleMarket.bbidoleMarket.api.login.dto.LoginReqDto;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.login.dto.LoginResDto;
import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
import com.bbidoleMarket.bbidoleMarket.common.config.JwtUtil;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.exception.UnauthorizedException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus.*;
import static com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus.*;

@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "로그인", description = "로그인 관련 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResDto>> login(@Valid @RequestBody LoginReqDto loginReqDto) {
        try {
            User user = userService.login(loginReqDto);

            if(!user.getIsActive()) {
                LoginResDto result = new LoginResDto(
                        null,
                        null,
                        Role.BAN
                );
                return ApiResponse.success(SUCCESS_LOGIN_BUT_BAN, result);
            }

            String accessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getRole().getKey());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

            LoginResDto result = new LoginResDto(
                    accessToken,
                    refreshToken,
                    user.getRole()
            );

            return ApiResponse.success(SEND_LOGIN_SUCCESS, result);
        } catch (NotFoundException e) {
            throw new NotFoundException("사용자를 찾을 수 없습니다.");
        } catch (BadRequestException e) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Operation(summary = "로그아웃", description = "현재 로그인한 사용자를 로그아웃합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        String accessToken = jwtUtil.extractTokenFromRequest(request);
        String refreshToken = request.getHeader("Refresh-Token");
        
        if (accessToken != null) {
            // 액세스 토큰을 블랙리스트에 추가
            jwtUtil.invalidateToken(accessToken);
        }
        
        if (refreshToken != null) {
            // 리프레시 토큰도 블랙리스트에 추가
            jwtUtil.invalidateToken(refreshToken);
        }
        
        return ApiResponse.success_only(SEND_LOGOUT_SUCCESS);
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResDto>> refreshToken(HttpServletRequest request) {
        try {
            // Authorization 헤더에서 리프레시 토큰 추출
            String refreshToken = jwtUtil.extractTokenFromRequest(request);

            if (refreshToken == null) {
                throw new UnauthorizedException("리프레시 토큰이 없습니다.");
            }

            if (!jwtUtil.validateToken(refreshToken)) {
                throw new UnauthorizedException("유효하지 않은 리프레시 토큰입니다.");
            }

            String userId = jwtUtil.extractUserId(refreshToken);
            User user = userService.findById(Long.parseLong(userId));

            // 새로운 토큰들 생성
            String newAccessToken = jwtUtil.generateAccessToken(user.getId().toString(), user.getRole().getKey());
            String newRefreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

            // 기존 리프레시 토큰을 무효화 (보안상 중요)
            jwtUtil.invalidateToken(refreshToken);

            LoginResDto result = new LoginResDto(
                    newAccessToken,
                    newRefreshToken,
                    user.getRole()
            );

            return ApiResponse.success(SEND_TOKEN_REFRESH_SUCCESS, result);
        } catch (Exception e) {
            throw new UnauthorizedException("토큰 갱신에 실패했습니다.");
        }
    }

    @Operation(summary = "이메일 중복 체크", description = "회원가입 시 이메일 중복 여부를 확인합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 사용 중인 이메일")
    })
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailDuplicate(
            @Parameter(description = "확인할 이메일") @RequestParam String email) {
        boolean isAvailable = userService.isEmailAvailable(email);
        if (!isAvailable) {
            return ApiResponse.error(ALREADY_REGISTER_EMAIL_EXCEPTION, false);
        }
        return ApiResponse.success(SEND_EMAIL_CHECK_SUCCESS, true);
    }

    @Operation(summary = "토큰 상태 확인 (개발용)", description = "토큰이 블랙리스트에 있는지 확인합니다.")
    @GetMapping("/token-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkTokenStatus(HttpServletRequest request) {
        String accessToken = jwtUtil.extractTokenFromRequest(request);
        String refreshToken = request.getHeader("Refresh-Token");
        
        Map<String, Object> status = new HashMap<>();
        
        if (accessToken != null) {
            status.put("accessToken", Map.of(
                "exists", true,
                "isBlacklisted", jwtUtil.isTokenInvalidated(accessToken),
                "isExpired", jwtUtil.isTokenExpired(accessToken),
                "isValid", jwtUtil.validateToken(accessToken)
            ));
        } else {
            status.put("accessToken", Map.of("exists", false));
        }
        
        if (refreshToken != null) {
            status.put("refreshToken", Map.of(
                "exists", true,
                "isBlacklisted", jwtUtil.isTokenInvalidated(refreshToken),
                "isExpired", jwtUtil.isTokenExpired(refreshToken),
                "isValid", jwtUtil.validateToken(refreshToken)
            ));
        } else {
            status.put("refreshToken", Map.of("exists", false));
        }
        
        return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                .status(200)
                .success(true)
                .message("토큰 상태 조회 성공")
                .data(status)
                .build());
    }
}
