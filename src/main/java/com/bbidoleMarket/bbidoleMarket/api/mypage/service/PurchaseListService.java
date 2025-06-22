package com.bbidoleMarket.bbidoleMarket.api.mypage.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
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
    public List<PurchaseListResDto> getPurchaseList(Long userId) {
    //chatRoom 테이블에서
        List<ChatRoom> chats = chatListRepository.findByBuyerIdAndIsCompleted(userId,true);
        List<Post> posts = new ArrayList<>();
        for(ChatRoom chat: chats ){
            posts.add(chat.getPost());
        }

    }
}
