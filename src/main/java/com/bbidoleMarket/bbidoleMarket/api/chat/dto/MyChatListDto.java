package com.bbidoleMarket.bbidoleMarket.api.chat.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyChatListDto {

    private Long id;
    private Long productId;
    private String productName;
    private Long sellerId;
    private String sellerName;
    private Long buyerId;
    private String buyerName;
    private Long othersId;
    private boolean isCompleted;
    private String lastMessage;
    private LocalDateTime lastMessageSendAt;
}
