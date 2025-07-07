package com.bbidoleMarket.bbidoleMarket.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private Integer price = 0;

    @Column(name = "post_image_url")
    private String imageUrl;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_sold", nullable = false)
    private Boolean isSold = false;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public static Post createPost
        (String title, int price, String imageUrl, String description, User user) {
        Post post = new Post();
        post.title = title;
        post.price = price;
        post.imageUrl = imageUrl;
        post.description = description;
        post.user = user;
        return post;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void updatePost(String title, int price, String description, String imageUrl) {
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public void updatePost(String title, int price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public void sold() {
        isSold = true;
    }

    public void unSold() {
        isSold = false;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public void markAsUndeleted() {
        isDeleted = false;
    }
}
