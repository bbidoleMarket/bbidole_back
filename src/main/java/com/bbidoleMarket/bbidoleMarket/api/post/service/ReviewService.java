package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.Review;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.ReviewRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
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
    private final UserRepository userRepository;

    // TODO 삭제
    @Transactional
    public Long save(ReviewReqDto dto) {
        User reviewer = userRepository.findById(dto.getReviewerId())
            .orElseThrow(() -> new NotFoundException("해당 id를 가진 사용자를 찾을 수 없습니다."));
        User reviewee = userRepository.findById(dto.getRevieweeId())
            .orElseThrow(() -> new NotFoundException("해당 id를 가진 사용자를 찾을 수 없습니다."));

        Review review = Review.createReview(dto.getRating(), dto.getContent(), reviewer, reviewee);
        return reviewRepository.save(review).getId();
    }

    public PageResDto<ReviewResDto> getReviewWithSeller(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.findByRevieweeId(id, pageable);
        Page<ReviewResDto> reviewDtoPage = reviewPage.map(ReviewResDto::fromReview);
        return new PageResDto<>(reviewDtoPage);
    }
}
