package com.bbidoleMarket.bbidoleMarket.api.chat.controller;

import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.MyChatListDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.service.ChatService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5137")
@RequestMapping("/api/chat")
public class chatController {
    private final ChatService chatService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MyChatListDto>>> getMyChatList(@AuthenticationPrincipal UserDetails user) {
        return ApiResponse.success(SuccessStatus.GET_CHAT_LIST_SUCCESS, chatService.getMyChatlist(user.getId()));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getChatRoomContents(@PathVariable Long chatId) {
        return ApiResponse.success(SuccessStatus.GET_CHAT_MSG_SUCCESS, chatService.getChatMessages(chatId));
    }

    @PatchMapping("/sold")
    public ResponseEntity<ApiResponse<Void>> setSold(@RequestParam Long id){
        chatService.setSold(id);
        return ApiResponse.success_only(SuccessStatus.SOLD_OUT_SUCCESS);
    }
}
