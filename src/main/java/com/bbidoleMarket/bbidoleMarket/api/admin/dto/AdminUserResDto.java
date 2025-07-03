package com.bbidoleMarket.bbidoleMarket.api.admin.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Role;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResDto {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private Role role;
    private LocalDateTime createdAt;
    private Boolean isActive;

    public AdminUserResDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.isActive = user.getIsActive();
    }
}
