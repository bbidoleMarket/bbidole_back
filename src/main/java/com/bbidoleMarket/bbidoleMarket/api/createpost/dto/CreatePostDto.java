package com.bbidoleMarket.bbidoleMarket.api.createpost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {
    private String title;        // 물품 이름
    private Integer price;       // 가격
    private String description;  // 물품 설명
}