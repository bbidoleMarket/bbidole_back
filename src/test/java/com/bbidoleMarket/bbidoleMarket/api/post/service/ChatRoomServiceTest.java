//package com.bbidoleMarket.bbidoleMarket.api.post.service;
//
//import com.bbidoleMarket.bbidoleMarket.api.login.service.UserService;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.ChatRoomReqDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.ChatRoomResDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSaveReqDto;
//import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
//import com.bbidoleMarket.bbidoleMarket.common.TestUtils;
//import java.util.List;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class ChatRoomServiceTest {
//
//    @Autowired
//    private ChatRoomService chatRoomService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private PostService postService;
//
//    @Test
//    public void 채팅방_생성_테스트() throws Exception {
//        // given
//        List<SigninDto> users = TestUtils.signinMany(2);
//        Long sellerId = userService.signin(users.get(0));
//        Long buyerId = userService.signin(users.get(1));
//
//        PostSaveReqDto post = TestUtils.createPostOne(sellerId);
//        Long postId = postService.save(post);
//
//        ChatRoomReqDto dto = new ChatRoomReqDto();
//        dto.setProductId(postId);
//        dto.setBuyerId(buyerId);
//        dto.setSellerId(sellerId);
//
//        // when
//        Long chatRoomId = chatRoomService.startChatRoom(dto);
//
//        // then
//        ChatRoomResDto chatRoomResDto = chatRoomService.findById(chatRoomId);
//        Assertions.assertEquals(chatRoomResDto.getProductId(), postId);
//        Assertions.assertEquals(chatRoomResDto.getBuyerId(), buyerId);
//        Assertions.assertEquals(chatRoomResDto.getSellerId(), sellerId);
//    }
//}
