package com.bbidoleMarket.bbidoleMarket.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank
    @Size(min = 2, max = 30)
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "total_rating")
    private Double totalRating = -1.0;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER; // 기본값은 일반 사용자

//    @OneToMany(mappedBy = "user")
//    private List<Post> posts = new ArrayList<>();
//
//    @OneToMany(mappedBy = "reviewee")
//    private List<Review> reviews = new ArrayList<>();

    @Builder
    public static User createUser(String name, String email, String password, String nickname,
        PasswordEncoder passwordEncoder) {
        // TODO email, password 형식 한번 더 검사하기
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = passwordEncoder.encode(password);
        user.nickname = nickname;
        user.role = Role.USER; // 기본값은 일반 사용자
        return user;
    }

    // public static User createAdmin(String name, String email, String password, String nickname, PasswordEncoder passwordEncoder) {
    //     // 관리자 계정 생성
    //     User admin = new User();
    //     admin.name = name;
    //     admin.email = email;
    //     admin.password = passwordEncoder.encode(password);
    //     admin.nickname = nickname;
    //     admin.totalRating = -1.0;
    //     admin.role = Role.ADMIN; // 관리자 권한
    //     return admin;
    // }

    // 관리자 여부 확인 메서드
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    // 사용자 권한 업데이트 메서드
    public void updateRole(Role role) {
        this.role = role;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        // TODO password 형식 검사하기
        this.password = passwordEncoder.encode(password);
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
