package com.bbidoleMarket.bbidoleMarket.api.signup.dto;

import lombok.Data;

@Data
public class SignupReqDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
}
