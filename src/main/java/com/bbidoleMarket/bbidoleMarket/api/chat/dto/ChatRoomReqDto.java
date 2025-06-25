package com.bbidoleMarket.bbidoleMarket.api.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class ChatRoomReqDto {
    private Long postId;
    private Long sellerId;
    private Long buyerId;
}
