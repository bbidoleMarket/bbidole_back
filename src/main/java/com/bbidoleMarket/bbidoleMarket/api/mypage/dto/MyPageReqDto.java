package com.bbidoleMarket.bbidoleMarket.api.mypage.dto;

import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor
@AllArgsConstructor
public class MyPageReqDto {
//    private String name;
    private String nickname;
    private String password;
    private String passwordConfirm;
    private String profileImage;

    private String email;

    //입력값 빈칸 검증
    public void validate(){
        if(isBlank(nickname) || isBlank(password) || isBlank(passwordConfirm))
            throw new BadRequestException("회원 정보 중 빈 값이 존재합니다.");
    }
    private boolean isBlank(String value){
        return value ==null || value.trim().isBlank();
    }
}
