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
public class PostSaveReqDto {

    private long userId;
    private String title;
    private String description;
    private int price;
    private String imageUrl;

}