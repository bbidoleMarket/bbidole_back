package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewDto;
import com.bbidoleMarket.bbidoleMarket.api.post.service.ReviewService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<PageResDto<ReviewDto>>> getReviewWithSeller(
        @PathVariable Long userId,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return ApiResponse
            .success(SuccessStatus.SEARCH_REVIEW_SUCCESS,
                reviewService.getReviewWithSeller(userId, page, size));
    }

}
