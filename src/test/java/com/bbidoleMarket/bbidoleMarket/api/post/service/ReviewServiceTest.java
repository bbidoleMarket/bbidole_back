//package com.bbidoleMarket.bbidoleMarket.api.post.service;
//
//import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewReqDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewResDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
//import com.bbidoleMarket.bbidoleMarket.common.TestUtils;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//class ReviewServiceTest {
//
//    @Autowired
//    UserService userService;
//    @Autowired
//    PostService postService;
//    @Autowired
//    ReviewService reviewService;
//
//
//    @Test
//    public void 판매자의_리뷰_조회_테스트_페이지네이션() throws Exception {
//        // given
//        List<SigninDto> signinDtos = TestUtils.signinMany(2);
//        List<Long> ids = new ArrayList<>();
//        for (SigninDto signinDto : signinDtos) {
//            ids.add(userService.signin(signinDto));
//        }
//
//        List<ReviewReqDto> reviewDtos = TestUtils.createReviewMany(ids.get(0), ids.get(1), 25);
//        for (ReviewReqDto reviewDto : reviewDtos) {
//            reviewService.save(reviewDto);
//        }
//
//        // when
//        PageResDto<ReviewResDto> result = reviewService.getReviewWithSeller(ids.get(1), 0,
//            20);
//
//        // then
//        Assertions.assertEquals(25, result.getTotalElements());
//        Assertions.assertEquals(2, result.getTotalPages());
//        Assertions.assertEquals(0, result.getPageNumber());
//        Assertions.assertEquals(20, result.getPageSize());
//
//
//    }
//}