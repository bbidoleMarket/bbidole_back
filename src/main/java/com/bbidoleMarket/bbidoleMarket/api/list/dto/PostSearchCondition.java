package com.bbidoleMarket.bbidoleMarket.api.list.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchCondition {
    private String keyword;     // 검색 키워드
    private Boolean onlySelling; // 판매중인 상품만 조회 (isSold=false)

    @Builder.Default
    private String sort = "latest"; // 정렬 옵션 (latest: 최신순, priceAsc: 가격낮은순, priceDesc: 가격높은순)

    @Builder.Default
    private Integer page = 0;   // 페이지 번호 (0부터 시작)

    @Builder.Default
    private Integer size = 20;  // 페이지 크기
}