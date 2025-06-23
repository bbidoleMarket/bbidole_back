package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.Review;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public PageResDto<ReviewDto> getReviewWithSeller(Long id, int page, int size) {
        // TODO page, size에 대한 검증 필요
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.findByRevieweeId(id, pageable);
        Page<ReviewDto> reviewDtoPage = reviewPage.map(ReviewDto::fromReview);
        return new PageResDto<>(reviewDtoPage);
    }
}
