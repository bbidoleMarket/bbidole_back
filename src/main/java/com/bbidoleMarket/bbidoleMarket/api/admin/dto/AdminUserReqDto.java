package com.bbidoleMarket.bbidoleMarket.api.admin.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserReqDto {
    private Long Id;
    private Boolean isActive;
    private Role role;
}
