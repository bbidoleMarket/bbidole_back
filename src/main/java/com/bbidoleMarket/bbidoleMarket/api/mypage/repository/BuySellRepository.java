package com.bbidoleMarket.bbidoleMarket.api.mypage.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuySellRepository extends JpaRepository<Post,Long> {

    //최신순(내림차순)
    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    //판매완료/판매중
    Page<Post> findByUserIdAndIsSoldOrderByCreatedAtDesc(Long userId,Boolean isSold,Pageable pageable);
}
