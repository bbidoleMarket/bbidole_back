package com.bbidoleMarket.bbidoleMarket.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "total_rating")
    private Double totalRating;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "user")
//    private List<Post> posts = new ArrayList<>();
//
//    @OneToMany(mappedBy = "reviewee")
//    private List<Review> reviews = new ArrayList<>();

    @Builder
    public static User createUser(String name, String email, String password, String nickname) {
        // TODO email, password 형식 한번 더 검사하기
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        user.nickname = nickname;
        user.totalRating = -1.0;
        return user;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void updatePassword(String password) {
        // TODO password 형식 검사하기
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateTotalRating(Double totalRating) {
        // TODO Review 생성할 때 마다 수정
        this.totalRating = totalRating;
    }
}

