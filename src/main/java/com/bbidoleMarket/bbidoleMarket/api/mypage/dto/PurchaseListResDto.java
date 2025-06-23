package com.bbidoleMarket.bbidoleMarket.api.mypage.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseListResDto {
//    private Long postId;
    private String title;
    private int price;
    private String imageUrl;
    private boolean isSold;

    public PurchaseListResDto(String title, int price, String imageUrl){
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
