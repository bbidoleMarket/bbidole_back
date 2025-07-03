package com.bbidoleMarket.bbidoleMarket.api.post.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import java.util.List;
import java.util.Optional;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    Optional<Post> findById(Long id);

    List<Post> findByUserId(Long userId);

    Post save(Post post); // 생성 및 수정

    Page<Post> findByUserId(Long UserId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user")
    List<Post> findAllWithUser();

    @Query("SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.createdAt DESC")
    List<Post> findTop5WithUser(Pageable pageable);
}
