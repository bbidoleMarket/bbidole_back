package com.bbidoleMarket.bbidoleMarket.api.login.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답 DTO")
public class LoginResDto {
    
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
    
    @Schema(description = "관리자 여부", example = "false")
    private Boolean isAdmin;
    
    @Schema(description = "리다이렉트 URL", example = "/admin")
    private String redirectUrl;

    // 편의 생성자
    public LoginResDto(String accessToken, String refreshToken, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isAdmin = (role == Role.ADMIN);
        this.redirectUrl = this.isAdmin ? "/admin" : "/";
    }
}
