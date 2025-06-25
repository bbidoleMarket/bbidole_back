package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// TODO 삭제
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SigninDto {

    private String email;
    private String password;
    private String name;

    public User asUser() {
        return User.createUser(name, email, password, name);
    }
}
