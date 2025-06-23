package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void 회원가입_및_회원조회_테스트() throws Exception {
        // given
        String email = "a@a.a";
        String password = "aaa1";
        String name = "aaa";

        SigninDto signinDto = new SigninDto();
        signinDto.setEmail(email);
        signinDto.setPassword(password);
        signinDto.setName(name);

        // when
        Long savedId = userService.signin(signinDto);

        User user = userService.findById(savedId);

        // then
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertEquals(name, user.getName());
    }

}