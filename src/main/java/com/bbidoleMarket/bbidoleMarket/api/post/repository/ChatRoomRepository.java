package com.bbidoleMarket.bbidoleMarket.api.post.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom save(ChatRoom chatRoom);

    List<ChatRoom> findAllByPostIdAndBuyerIdAndSellerIdAndIsCompleted(Long postId,
        Long buyerId, Long sellerId, Boolean completed);

    @Override
    Optional<ChatRoom> findById(Long chatRoomID);
}
