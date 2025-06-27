package com.bbidoleMarket.bbidoleMarket.api.chat.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoomId(Long chatId);
    
    ChatMessage findTopByChatRoomIdOrderBySendAtDesc(Long chatRoomId);
}
