package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatRoomReqDto {

    private Long productId;
    private Long buyerId;
    private Long sellerId;
}
