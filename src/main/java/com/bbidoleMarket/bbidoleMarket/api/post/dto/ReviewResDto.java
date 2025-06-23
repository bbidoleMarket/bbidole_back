package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewResDto {

    private String writerNickname;
    private String content;
    private int rating;

    public static ReviewResDto fromReview(Review review) {
        ReviewResDto dto = new ReviewResDto();
        dto.setWriterNickname(review.getReviewer().getNickname());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        return dto;
    }

}
