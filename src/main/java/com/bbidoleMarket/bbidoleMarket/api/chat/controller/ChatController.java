package com.bbidoleMarket.bbidoleMarket.api.chat.controller;

import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageResDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatRoomReqDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.MyChatListDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.service.ChatService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MyChatListDto>>> getMyChatList(@AuthenticationPrincipal String strUserId) {
        Long userId = null;
        try {
            userId = Long.parseLong(strUserId);
        } catch (NumberFormatException e) {
            System.out.println("숫자로 변환할 수 없습니다.");
        }
        return ApiResponse.success(SuccessStatus.GET_CHAT_LIST_SUCCESS, chatService.getMyChatlist(userId));
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<MyChatListDto>> startChat(@RequestBody final ChatRoomReqDto chatRoomReqDto) {
        return ApiResponse.success(SuccessStatus.GET_CHAT_ROOM_SUCCESS, chatService.startChatRoom(chatRoomReqDto));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResDto>>> getChatRoomContents(@PathVariable Long chatId) {
        return ApiResponse.success(SuccessStatus.GET_CHAT_MSG_SUCCESS, chatService.getChatMessages(chatId));
    }

    @PatchMapping("/sold")
    public ResponseEntity<ApiResponse<Void>> setSold(@RequestParam Long id){
        chatService.setSold(id);
        return ApiResponse.success_only(SuccessStatus.SOLD_OUT_SUCCESS);
    }
}
