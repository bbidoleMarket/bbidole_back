package com.bbidoleMarket.bbidoleMarket.api.chat.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
public class MyChatListDto {
    private Long chatId;
    private String productName;
    private String sellerName;
    private String buyerName;
    private boolean isCompleted;
}
