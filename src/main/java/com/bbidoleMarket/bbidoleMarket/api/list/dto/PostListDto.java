package com.bbidoleMarket.bbidoleMarket.api.list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    private Long id;
    private String title;        // 물품 이름
    private Integer price;       // 가격
    private String imageUrl;     // 이미지 URL
    private LocalDateTime createdAt;  // 등록일시
    private boolean isSold;      // 판매 완료 여부
}