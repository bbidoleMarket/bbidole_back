package com.bbidoleMarket.bbidoleMarket.api.mypage.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseListResDto {
    private Long postId;
    private String title;
    private int price;
    private String imageUrl;
    private boolean isSold;
}
