package com.bbidoleMarket.bbidoleMarket.api.chat.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(Long chatId);
}
