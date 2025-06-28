package com.bbidoleMarket.bbidoleMarket.api.chat.service;

import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageResDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatSocketDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatRepository;
import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WebSocketService {
    private final ObjectMapper objectMapper;
    private final ChatRepository chatRepository;
    private Map<Long, ChatSocketDto> chatRoomMap;

    @PostConstruct
    public void init() {
        this.chatRoomMap = new LinkedHashMap<>();
    }

    private ChatSocketDto setAndFindChatRoomMapById(Long chatId) {
        ChatRoom chatRoom = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.CHAT_NOT_FOUND_EXCEPTION.getMessage()));

        ChatSocketDto chatSocketDto = ChatSocketDto.builder()
                .chatId(chatId)
                .name(chatRoom.getPost().getTitle())
                .sellerId(chatRoom.getSeller().getId())
                .buyerId(chatRoom.getBuyer().getId())
                .build();
        chatRoomMap.put(chatId, chatSocketDto);
        return chatSocketDto;
    }

    private ChatSocketDto findChatRoomById(Long chatId) {
        return chatRoomMap.get(chatId);
    }

    public void addSessionAndHandleEnter(Long chatId, WebSocketSession session) {
        ChatSocketDto room = findChatRoomById(chatId);
        if (room == null) {
            room = setAndFindChatRoomMapById(chatId);
        }

        room.getSessions().add(session);
        log.debug("New session added: " + session);
    }

    public void sendMessage(Long chatId, ChatMessageResDto message) {
        ChatSocketDto room = findChatRoomById(chatId);
        if (room != null) {
            for (WebSocketSession session : room.getSessions()) {
                try {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    public void broadcastSoldEvent(Long chatId) {
       ChatSocketDto room = findChatRoomById(chatId);
       if (room != null) {
           for (WebSocketSession session : room.getSessions()) {
               try {
                   session.sendMessage(new TextMessage("{\"type\":\"sold\"}"));
               } catch (IOException e) {
                   log.error(e.getMessage());
               }
           }
       }
    }

    // 채팅방에서 퇴장한 세션 제거
    public void removeSessionAndHandleExit(Long chatId, WebSocketSession session) {
        ChatSocketDto room = findChatRoomById(chatId); // 채팅방 정보 가져오기
        if (room != null) {
            room.getSessions().remove(session); // 채팅방에서 퇴장한 세션 제거
            log.debug("Session removed: " + session);
            if (room.isSessionEmpty()) {
                chatRoomMap.remove(chatId);
            }
        }
    }
}
