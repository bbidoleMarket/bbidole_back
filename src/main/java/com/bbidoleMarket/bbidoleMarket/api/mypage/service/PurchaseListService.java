package com.bbidoleMarket.bbidoleMarket.api.mypage.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dao.PurchaseListDao;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PurchaseListResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.ChatListRepository;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.PostRepository;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.PurchaseListRepository;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseListService {
    private final UserRepository userRepository;
    private final PurchaseListRepository purchaseListRepository;
    private final PostRepository postRepository;
    private final ChatListRepository chatListRepository;


    //구매 목록 조회
//    public List<PurchaseListResDto> getPurchaseList(Long userId) {
//    //chatRoom 테이블에서 buy_id == user_id, is_completed = true일 때
//        List<ChatRoom> chats = chatListRepository.findByBuyerIdAndIsCompleted(userId,true);
//        List<PurchaseListResDto> posts = new ArrayList<>();
//        for(ChatRoom chat: chats ){
//            //영속성 컨텍스트 종료되어 에러
//            Post post = chat.getPost();
//
//            PurchaseListResDto purchaseListResDto = new PurchaseListResDto();
//            purchaseListResDto.setTitle(post.getTitle());
//            purchaseListResDto.setPrice(post.getPrice());
//            purchaseListResDto.setImageUrl(post.getImageUrl());
//            posts.add(purchaseListResDto);
//        }
//        return posts;
//    }
    private final PurchaseListDao purchaseListDao;
    public List<PurchaseListResDto> getPurchaseList(Long userId){
        return purchaseListDao.findPurchaseListByBuyerId(userId);
    }
}
