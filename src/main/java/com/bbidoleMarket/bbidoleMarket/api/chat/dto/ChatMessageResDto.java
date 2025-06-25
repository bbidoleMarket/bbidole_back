package com.bbidoleMarket.bbidoleMarket.api.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor
public class ChatMessageResDto {
    private Long chatId;
    private String content;
    private Long senderId;
    private LocalDateTime sendAt;
}
