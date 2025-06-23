package com.bbidoleMarket.bbidoleMarket.api.mypage.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
