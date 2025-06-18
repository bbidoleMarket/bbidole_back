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

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Integer price;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "is_sold")
    private Boolean isSold;

//    @Column(name = "post_status")
//    @Enumerated(value = EnumType.STRING)
//    private PostStatus status; // [ORDINAL, SOLD, TEMP]

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public static Post createPost(String title, int price, String description, User user) {
        Post post = new Post();
        post.title = title;
        post.price = price;
        post.description = description;
        post.isSold = false;
//        post.status = PostStatus.ORDINAL;
        post.user = user;
        return post;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void updatePost(String title, int price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public void sold() {
        isSold = true;
//        post.status = PostStatus.SOLD;
    }

    public void unSold() {
        isSold = false;
//        post.status = PostStatus.ORDINAL;
    }
}
