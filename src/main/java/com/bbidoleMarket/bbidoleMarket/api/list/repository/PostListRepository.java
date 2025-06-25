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

    // 판매중인 상품만 조회 - 최신순
    Page<Post> findByIsSoldOrderByCreatedAtDesc(boolean isSold, Pageable pageable);

    // 판매중인 상품만 조회 - 가격 오름차순
    Page<Post> findByIsSoldOrderByPriceAsc(boolean isSold, Pageable pageable);

    // 판매중인 상품만 조회 - 가격 내림차순
    Page<Post> findByIsSoldOrderByPriceDesc(boolean isSold, Pageable pageable);

    // 모든 상품 조회 - 최신순
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 모든 상품 조회 - 가격 오름차순
    Page<Post> findAllByOrderByPriceAsc(Pageable pageable);

    // 모든 상품 조회 - 가격 내림차순
    Page<Post> findAllByOrderByPriceDesc(Pageable pageable);

    // 검색 기능 - 최신순
    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND (:onlySelling = false OR p.isSold = false) ORDER BY p.createdAt DESC")
    Page<Post> searchPostsByLatest(
            @Param("keyword") String keyword,
            @Param("onlySelling") Boolean onlySelling,
            Pageable pageable);

    // 검색 기능 - 가격 오름차순
    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND (:onlySelling = false OR p.isSold = false) ORDER BY p.price ASC")
    Page<Post> searchPostsByPriceAsc(
            @Param("keyword") String keyword,
            @Param("onlySelling") Boolean onlySelling,
            Pageable pageable);

    // 검색 기능 - 가격 내림차순
    @Query("SELECT p FROM Post p WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND (:onlySelling = false OR p.isSold = false) ORDER BY p.price DESC")
    Page<Post> searchPostsByPriceDesc(
            @Param("keyword") String keyword,
            @Param("onlySelling") Boolean onlySelling,
            Pageable pageable);
}