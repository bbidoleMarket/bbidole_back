package com.bbidoleMarket.bbidoleMarket.api.post.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 삭제되지 않은 게시글만 조회하는 메소드들 추가
    Optional<Post> findByIdAndIsDeletedFalse(Long id);
    Page<Post> findAllByIsDeletedFalse(Pageable pageable);
    
    // 이미 존재하는 다른 조회 메소드에 isDeleted 조건 추가
    // 예: 사용자별 게시글 조회
    Page<Post> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

    @Override
    Optional<Post> findById(Long id);

    List<Post> findByUserId(Long userId);

    Post save(Post post); // 생성 및 수정

    Page<Post> findByUserId(Long UserId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.isDeleted = false")
    List<Post> findAllWithUser();

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.isDeleted = false ORDER BY p.createdAt DESC")
    List<Post> findTop5WithUser(Pageable pageable);

    // 추가할 메서드
    List<Post> findByUserIdAndIsDeletedFalse(Long userId);
}
