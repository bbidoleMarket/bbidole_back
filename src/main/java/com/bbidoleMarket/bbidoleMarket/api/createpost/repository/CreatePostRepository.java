package com.bbidoleMarket.bbidoleMarket.api.createpost.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatePostRepository extends JpaRepository<Post, Long> {
}