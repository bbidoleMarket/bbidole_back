//package com.bbidoleMarket.bbidoleMarket.common;
//
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSaveReqDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.ReviewReqDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestUtils {
//
//    // User
//    public static final String email = "b@b.b";
//    public static final String password = "bbb!";
//    public static final String name = "bbb";
//
//    // Post
//    public static final String title = "title";
//    public static final int price = 1000;
//    public static final String imageUrl = "imageUrl";
//    public static final String description = "description";
//
//    // Review
//    public static final String content = "content";
//
//    public static SigninDto signinOne() {
//
//        SigninDto signinDto = new SigninDto();
//        signinDto.setEmail(email);
//        signinDto.setPassword(password);
//        signinDto.setName(name);
//
//        return signinDto;
//    }
//
//    public static List<SigninDto> signinMany(int count) {
//        List<SigninDto> signinDtos = new ArrayList<>();
//
//        if (count == 1) {
//            signinDtos.add(signinOne());
//            return signinDtos;
//        }
//
//        for (int i = 0; i < count; i++) {
//            SigninDto signinDto = new SigninDto();
//            signinDto.setEmail(email + i);
//            signinDto.setPassword(password + i);
//            signinDto.setName(name + i);
//
//            signinDtos.add(signinDto);
//        }
//        return signinDtos;
//    }
//
//    public static PostSaveReqDto createPostOne(long userId) {
//        PostSaveReqDto postSaveReqDto = new PostSaveReqDto();
//        postSaveReqDto.setTitle(title);
//        postSaveReqDto.setPrice(price);
//        postSaveReqDto.setImageUrl(imageUrl);
//        postSaveReqDto.setDescription(description);
//        postSaveReqDto.setUserId(userId);
//        return postSaveReqDto;
//    }
//
//    public static List<PostSaveReqDto> createPostMany(long userId, int count) {
//        List<PostSaveReqDto> postSaveReqDtos = new ArrayList<>();
//
//        if (count == 1) {
//            postSaveReqDtos.add(createPostOne(userId));
//            return postSaveReqDtos;
//        }
//
//        for (int i = 0; i < count; i++) {
//            PostSaveReqDto postSaveReqDto = new PostSaveReqDto();
//            postSaveReqDto.setTitle(title + i);
//            postSaveReqDto.setPrice(price + i);
//            postSaveReqDto.setImageUrl(imageUrl + i);
//            postSaveReqDto.setDescription(description + i);
//            postSaveReqDto.setUserId(userId);
//
//            postSaveReqDtos.add(postSaveReqDto);
//        }
//
//        return postSaveReqDtos;
//    }
//
//    public static List<ReviewReqDto> createReviewMany(long reviewerId, long revieweeId, int count) {
//        List<ReviewReqDto> reviewReqDtos = new ArrayList<>();
//
//        for (int i = 0; i < count; i++) {
//            ReviewReqDto reviewReqDto = new ReviewReqDto();
//
//            reviewReqDto.setReviewerId(reviewerId);
//            reviewReqDto.setRevieweeId(revieweeId);
//            reviewReqDto.setRating(i % 6);
//            reviewReqDto.setContent(content + i);
//            reviewReqDtos.add(reviewReqDto);
//        }
//
//        return reviewReqDtos;
//    }
//
//}
