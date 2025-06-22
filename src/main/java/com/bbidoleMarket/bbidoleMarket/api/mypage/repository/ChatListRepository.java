package com.bbidoleMarket.bbidoleMarket.api.mypage.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatListRepository extends JpaRepository<ChatRoom,Long> {
    List<ChatRoom> findByBuyerIdAndIsCompleted(Long buyerId, Boolean isCompleted);
}
