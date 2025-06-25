package com.bbidoleMarket.bbidoleMarket.api.signup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "회원가입 요청 DTO")
public class SignupReqDto {
    
    @Schema(description = "이메일", example = "user@example.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;
    
    @Schema(description = "비밀번호", example = "Password1!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$])[a-zA-Z\\d!@#$]+$", 
             message = "비밀번호는 소문자, 대문자, 숫자, 특수문자(!@#$)를 포함해야 합니다.")
    private String password;
    
    @Schema(description = "이름", example = "홍길동")
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 4, message = "이름은 2자 이상 4자 이하로 입력해주세요.")
    private String name;
    
    @Schema(description = "닉네임", example = "길동이")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]+$", message = "닉네임은 한글, 영문, 숫자, 언더스코어, 하이픈만 사용 가능합니다.")
    private String nickname;
}
