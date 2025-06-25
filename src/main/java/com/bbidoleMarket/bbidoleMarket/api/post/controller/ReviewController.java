package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.service.ReviewService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "", description = "")
@Slf4j
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}")
    @Operation(summary = "특정 사용자의 리뷰를 조회")
    public ResponseEntity<ApiResponse<PageResDto<ReviewResDto>>> getReviewWithSeller(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse
            .success(SuccessStatus.SEARCH_REVIEW_SUCCESS,
                reviewService.getReviewWithSeller(userId, page, size));
    }

    @GetMapping
    @Operation(summary = "나의 리뷰를 조회")
    public ResponseEntity<ApiResponse<PageResDto<ReviewResDto>>> getReviewWithUserId(
            @AuthenticationPrincipal String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = Long.parseLong(id);
        return ApiResponse
                .success(SuccessStatus.SEARCH_REVIEW_SUCCESS,
                        reviewService.getReviewWithSeller(userId, page, size));
    }

}
