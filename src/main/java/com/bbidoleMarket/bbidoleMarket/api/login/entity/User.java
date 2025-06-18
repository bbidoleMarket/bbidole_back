package com.bbidoleMarket.bbidoleMarket.api.login.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(min = 2, max = 30)
    @Column(nullable = false, unique = true)
    private String nickname;

    private int totalRating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static User createUser(String name, String email, String password, String nickname, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = passwordEncoder.encode(password);
        user.nickname = nickname;
        return user;
    }

    public void updateTotalRating(int rating) {
        this.totalRating += rating;
    }
}
