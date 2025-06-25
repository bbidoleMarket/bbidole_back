package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.UserDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.service.UserService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "", description = "")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /*// TODO 삭제
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<Void>> signin(@RequestBody SigninDto dto) {
        userService.signin(dto);
        return ApiResponse.success_only(SuccessStatus.SEND_REGISTER_SUCCESS);
    }*/

//    @GetMapping("/detail")
//    @Operation(summary = "나의 상세정보를 요청")
//    public ResponseEntity<ApiResponse<UserDetailResDto>> getUserDetail() {
    // TODO JWT 합쳐진다음 다시 확인하기
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = auth.getUserId(); // 주로 email 또는 userId
//        return ApiResponse.success(SuccessStatus.SEARCH_USER_SUCCESS, userService.findUserById(userId));
//    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "판매자의 상세정보를 요청")
    public ResponseEntity<ApiResponse<UserDetailResDto>> getSeller(@PathVariable("id") Long id) {
        return ApiResponse
            .success(SuccessStatus.SEARCH_USER_SUCCESS, userService.findUserById(id));
    }

}

