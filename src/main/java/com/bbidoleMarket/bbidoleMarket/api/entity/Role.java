package com.bbidoleMarket.bbidoleMarket.api.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("USER", "일반 사용자"),
    ADMIN("ADMIN", "관리자"),
    BAN("BAN", "정지된 사용자");
    
    private final String key;
    private final String title;
} 