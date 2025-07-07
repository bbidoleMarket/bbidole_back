package com.bbidoleMarket.bbidoleMarket.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", referencedColumnName = "user_id")
    private User reviewer; // 리뷰를 단 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", referencedColumnName = "user_id")
    private User reviewee; // 상품을 올린 사람

    @Builder
    public static Review createReview(int rating, String content, User reviewer, User reviewee) {
        Review review = new Review();
        review.rating = rating;
        review.content = content;
        review.reviewer = reviewer;
        review.reviewee = reviewee;
        return review;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
