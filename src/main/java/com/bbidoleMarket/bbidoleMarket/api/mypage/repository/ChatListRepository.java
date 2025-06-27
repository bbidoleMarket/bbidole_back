package com.bbidoleMarket.bbidoleMarket.api.mypage.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatListRepository extends JpaRepository<ChatRoom,Long> {
    //최신순(내림차순)
    Page<ChatRoom> findBySellerIdOrderByCreatedAtDesc(Long buyerId,Pageable pageable);
    //판매완료
    Page<ChatRoom> findBySellerIdAndIsCompleted(Long sellerId, Boolean isCompleted,Pageable pageable);
    //판매중
    //ChatRoom과 연결된 Post 객체의 isSold를 조회
    Page<ChatRoom> findBySellerIdAndPost_IsSold(Long sellerId,Boolean isSold,Pageable pageable);

}
