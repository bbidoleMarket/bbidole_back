package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.ChatRoomReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.service.ChatRoomService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-room")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Long>> startChatRoom(
        @RequestBody ChatRoomReqDto chatRoomReqDto) {
        return ApiResponse.success(SuccessStatus.SEARCH_CHAT_ROOM_SUCCESS,
            chatRoomService.startChatRoom(chatRoomReqDto));
    }
}
