package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostUpdateReqDto {

    private long postId;
    private String title;
    private String description;
    private int price;
    private String imageUrl;
    private long userId;
}
