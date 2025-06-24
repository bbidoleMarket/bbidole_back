package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// TODO 삭제
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewReqDto {

    private Long reviewerId;
    private Long revieweeId;
    private String content;
    private int rating;

}
