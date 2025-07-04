package com.bbidoleMarket.bbidoleMarket.api.list.repository;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostListRepository extends JpaRepository<Post, Long> {

    // 판매중인 상품만 조회 - 최신순 (isDeleted=false 추가)
    Page<Post> findByIsSoldAndIsDeletedFalseOrderByCreatedAtDesc(boolean isSold, Pageable pageable);

    // 판매중인 상품만 조회 - 가격 오름차순 (isDeleted=false 추가)
    Page<Post> findByIsSoldAndIsDeletedFalseOrderByPriceAsc(boolean isSold, Pageable pageable);

    // 판매중인 상품만 조회 - 가격 내림차순 (isDeleted=false 추가)
    Page<Post> findByIsSoldAndIsDeletedFalseOrderByPriceDesc(boolean isSold, Pageable pageable);

    // 모든 상품 조회 - 최신순 (isDeleted=false 추가)
    Page<Post> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    // 모든 상품 조회 - 가격 오름차순 (isDeleted=false 추가)
    Page<Post> findByIsDeletedFalseOrderByPriceAsc(Pageable pageable);

    // 모든 상품 조회 - 가격 내림차순 (isDeleted=false 추가)
    Page<Post> findByIsDeletedFalseOrderByPriceDesc(Pageable pageable);

    // 검색 기능 - 최신순
    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND (:onlySelling IS NULL OR p.isSold = false) AND p.isDeleted = false ORDER BY p.createdAt DESC")
    Page<Post> searchPostsByLatest(
            @Param("keyword") String keyword,
            @Param("onlySelling") Boolean onlySelling,
            Pageable pageable);

    // 검색 기능 - 가격 오름차순
    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND (:onlySelling IS NULL OR p.isSold = false) AND p.isDeleted = false ORDER BY p.price ASC")
    Page<Post> searchPostsByPriceAsc(
            @Param("keyword") String keyword,
            @Param("onlySelling") Boolean onlySelling,
            Pageable pageable);

    // 검색 기능 - 가격 내림차순
    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND (:onlySelling IS NULL OR p.isSold = false) AND p.isDeleted = false ORDER BY p.price DESC")
    Page<Post> searchPostsByPriceDesc(
            @Param("keyword") String keyword,
            @Param("onlySelling") Boolean onlySelling,
            Pageable pageable);
}