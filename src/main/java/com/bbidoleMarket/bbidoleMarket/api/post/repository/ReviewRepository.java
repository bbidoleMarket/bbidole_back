package com.bbidoleMarket.bbidoleMarket.api.post.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByRevieweeId(Long revieweeId, Pageable pageable);

    // 리뷰어 ID로 리뷰를 최신순으로 정렬하여 조회
    Page<Review> findByRevieweeIdOrderByCreatedAtDesc(Long revieweeId, Pageable pageable);

    List<Review> findByRevieweeId(Long revieweeId);

    Review save(Review review);
}
