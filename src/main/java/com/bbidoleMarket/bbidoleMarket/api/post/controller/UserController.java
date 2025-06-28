package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.UserDetailResDto;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자", description = "사용자 정보에 관련된 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/detail")
    @Operation(summary = "나의 상세정보를 요청")
    public ResponseEntity<ApiResponse<UserDetailResDto>> getUserDetail(
        @AuthenticationPrincipal String id
    ) {
        Long userId = Long.parseLong(id); // 주로 email 또는 userId
        return ApiResponse
            .success(SuccessStatus.SEARCH_USER_SUCCESS, userService.findUserById(userId));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "판매자의 상세정보를 요청")
    public ResponseEntity<ApiResponse<UserDetailResDto>> getSeller(@PathVariable("id") Long id) {
        return ApiResponse
            .success(SuccessStatus.SEARCH_USER_SUCCESS, userService.findUserById(id));
    }

}

