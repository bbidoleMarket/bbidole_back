package com.bbidoleMarket.bbidoleMarket.api.chat.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByBuyerIdOrSellerId(Long userId, Long sellerId);

    ChatRoom findByPostIdAndBuyerIdAndSellerId(Long postId, Long buyerId, Long sellerId);
}
