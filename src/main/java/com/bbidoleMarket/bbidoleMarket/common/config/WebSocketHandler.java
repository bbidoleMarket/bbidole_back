package com.bbidoleMarket.bbidoleMarket.common.config;

import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageReqDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageResDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.service.ChatService;
import com.bbidoleMarket.bbidoleMarket.api.chat.service.WebSocketService;
import com.bbidoleMarket.bbidoleMarket.api.entity.ChatMessage;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final WebSocketService webSocketService;
    private final ChatService chatService;
    private final Map<WebSocketSession, Long> sessionRoomIdMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 새 연결이 맺어졌을 때
        System.out.println("새 연결: " + session.getId());
        try {
            Long chatId = extractChatIdFromUri(Objects.requireNonNull(session.getUri())); // 직접 파싱 구현 필요
            if (chatId == null) throw new NotFoundException(ErrorStatus.CHAT_NOT_FOUND_EXCEPTION.getMessage());

            sessionRoomIdMap.put(session, chatId);
            log.info("websocket connection established: " + chatId);
            webSocketService.addSessionAndHandleEnter(chatId, session); // 채팅방에 입장한 세션 추가
        } catch (Exception e) {
            log.error("websocket connection establish failed", e);
        }
    }

    private Long extractChatIdFromUri(URI uri) {
        String query = uri.getQuery();
        System.out.println("query: " + query);
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair[0].equals("chatId")) {
                    return Long.parseLong(pair[1]);
                }
            }
        }
        return null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 1. 메시지 파싱 (예: JSON → DTO)
        String payload = message.getPayload(); // 클라이언트가 전송한 메시지
        log.warn("{}", payload);
        ChatMessageReqDto parsedMessage = objectMapper.readValue(message.getPayload(), ChatMessageReqDto.class);

        // 2. 메시지 저장 (DB에 저장 등)
        ChatMessage chatMessage = chatService.convertToMessageEntity(parsedMessage);
        chatService.saveMessage(chatMessage);

        ChatMessageResDto chatMessageResDto = chatService.convertToChatMessageResDto(chatMessage);

        // 3. 메시지 전송 (브로드캐스트: 모든 세션에, 또는 같은 채팅방 세션에만)
        webSocketService.sendMessage(parsedMessage.getChatId(), chatMessageResDto);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 세션과 매핑된 채팅방 ID 가져오기
        log.warn("afterConnectionClosed : {}", session);
        Long chatId = sessionRoomIdMap.remove(session);
        if (chatId != null) {
            webSocketService.removeSessionAndHandleExit(chatId, session);
        }
    }
}
