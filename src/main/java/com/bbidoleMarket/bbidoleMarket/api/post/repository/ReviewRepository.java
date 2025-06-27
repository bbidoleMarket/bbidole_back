package com.bbidoleMarket.bbidoleMarket.api.post.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByRevieweeId(Long revieweeId, Pageable pageable);
    List<Review> findByRevieweeId(Long revieweeId);

    Review save(Review review);
}
