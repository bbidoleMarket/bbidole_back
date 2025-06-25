package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewDto {

    private long id;
    private String writerNickname;
    private String content;
    private int rating;

}
