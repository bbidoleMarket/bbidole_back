package com.bbidoleMarket.bbidoleMarket.api.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class ReviewReqDto {
    private Integer rating;
    private String content;
    private Long sellerId;
    private Long buyerId;
    private Long chatId;
}
