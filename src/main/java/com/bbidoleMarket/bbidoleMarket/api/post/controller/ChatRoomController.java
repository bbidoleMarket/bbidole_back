package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.ChatRoomReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.service.ChatRoomService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채팅방", description = "채팅방에 관련된 API")
@RestController
@RequestMapping("/api/chat-room")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/")
    @Operation(summary = "게시물에서 채팅방이 없다면 생성하고 있다면 조회하여 채팅방으로 넘어가기")
    public ResponseEntity<ApiResponse<Long>> startChatRoom(
        @RequestBody ChatRoomReqDto chatRoomReqDto) {
        return ApiResponse.success(SuccessStatus.SEARCH_CHAT_ROOM_SUCCESS,
            chatRoomService.startChatRoom(chatRoomReqDto));
    }
}
