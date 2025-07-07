package com.bbidoleMarket.bbidoleMarket.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_list")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "user_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "user_id")
    private User seller;

    @Column(name = "is_reviewed", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isReviewed = false;

    @Builder
    public static ChatRoom createChatRoom(Post post, User buyer, User seller) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.post = post;
        chatRoom.buyer = buyer;
        chatRoom.seller = seller;
        return chatRoom;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void completeChat() {
        isCompleted = true;
    }

    public void restartChat() {
        isCompleted = false;
    }

    public void markAsReviewed() {
        isReviewed = true;
    }

    public void markAsUnreviewed() {
        isReviewed = false;
    }
}
