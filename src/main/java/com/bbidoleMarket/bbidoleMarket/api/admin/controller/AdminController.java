package com.bbidoleMarket.bbidoleMarket.api.admin.controller;

import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminProductReqDto;
import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminProductResDto;
import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminUserReqDto;
import com.bbidoleMarket.bbidoleMarket.api.admin.dto.AdminUserResDto;
import com.bbidoleMarket.bbidoleMarket.api.entity.Role;
import com.bbidoleMarket.bbidoleMarket.common.config.RequireAdmin;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatRepository;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.login.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.UserDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.PostRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ChatRepository chatRepository;
    private final UserService userService;


    @RequireAdmin
    @GetMapping("/totalSub")
    public ResponseEntity<ApiResponse<Long>> getTotalSub() {
        Long total = null;
        try {
            total = userRepository.count();
            return ApiResponse.success(SuccessStatus.TOTAL_SUBS_SUCCESS, total);
        } catch (Exception e) {
            throw new BadRequestException("전체 회원 조회 오류 : " + e.getMessage());
        }
    }

    @RequireAdmin
    @GetMapping("/totalProduct")
    public ResponseEntity<ApiResponse<Long>> getTotalProduct() {
        Long total = null;
        try {
            total = postRepository.count();
            return ApiResponse.success(SuccessStatus.TOTAL_PRODUCT_SUCCESS, total);
        } catch (Exception e) {
            throw new BadRequestException("전체 물품 조회 오류 : " + e.getMessage());
        }
    }

    @RequireAdmin
    @GetMapping("/totalCompleted")
    public ResponseEntity<ApiResponse<Long>> getTotalCompleted() {
        Long total = null;
        try {
            total = chatRepository.countByIsCompletedTrue();
            return ApiResponse.success(SuccessStatus.TOTAL_SUBS_SUCCESS, total);
        } catch (Exception e) {
            throw new BadRequestException("거래 완료 물품 조회 오류 : " + e.getMessage());
        }
    }

    @RequireAdmin
    @GetMapping("/findAllUser")
    public ResponseEntity<ApiResponse<List<AdminUserResDto>>> findAllUser() {
        List<AdminUserResDto> dtos = userRepository.findAll().stream()
                .map(AdminUserResDto::new) // User → AdminUserResDto 변환
                .toList();
        return ApiResponse.success(SuccessStatus.FIND_ALL_USER_SUCCESS, dtos);
    }

    @RequireAdmin
    @GetMapping("/findAllProduct")
    public ResponseEntity<ApiResponse<List<AdminProductResDto>>> findAllProduct() {
        List<AdminProductResDto> dtos = postRepository.findAllWithUser().stream()
                .map(AdminProductResDto::new) // Post → AdminProductResDto 변환
                .toList();
        return ApiResponse.success(SuccessStatus.FIND_ALL_PRODUCT_SUCCESS, dtos);
    }

    @RequireAdmin
    @PostMapping("/controlIsActive")
    public ResponseEntity<ApiResponse<Void>> controlIsActive(@RequestBody AdminUserReqDto adminUserReqDto) {
        try {
            userService.updateUserActive(adminUserReqDto.getId(), adminUserReqDto.getIsActive());
            return ApiResponse.success_only(SuccessStatus.UPDATE_POST_SUCCESS);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("유저 활성 상태 변경 오류 : " + e.getMessage());
        }
    }

    @RequireAdmin
    @PostMapping("/deleteProduct")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@RequestBody AdminProductReqDto adminProductReqDto) {
        try {
            postRepository.deleteById(adminProductReqDto.getId());
            return ApiResponse.success_only(SuccessStatus.DELETE_PRODUCT_SUCCESS);
        } catch (Exception e) {
            throw new BadRequestException("제품 삭제 오류 : " + e.getMessage());
        }
    }

    @RequireAdmin
    @GetMapping("/recentUser")
    public ResponseEntity<ApiResponse<List<AdminUserResDto>>> recentUser() {
        List<AdminUserResDto> dtos = userRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(AdminUserResDto::new)
                .toList();
        return ApiResponse.success(SuccessStatus.FIND_RECENT_USER_SUCCESS, dtos);
    }

    @RequireAdmin
    @GetMapping("/recentProduct")
    public ResponseEntity<ApiResponse<List<Post>>> recentProduct() {
        List<Post> posts = postRepository.findTop5WithUser(PageRequest.of(0, 5));
        return ApiResponse.success(SuccessStatus.FIND_RECENT_PRODUCT_SUCCESS, posts);
    }
}
