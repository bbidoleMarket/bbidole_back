package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
import com.bbidoleMarket.bbidoleMarket.common.TestUtils;
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
        SigninDto signinDto = TestUtils.signinOne();

        // when
        Long savedId = userService.signin(signinDto);

        User user = userService.findById(savedId);

        // then
        Assertions.assertEquals(signinDto.getEmail(), user.getEmail());
        Assertions.assertEquals(signinDto.getPassword(), user.getPassword());
        Assertions.assertEquals(signinDto.getName(), user.getName());
    }

}