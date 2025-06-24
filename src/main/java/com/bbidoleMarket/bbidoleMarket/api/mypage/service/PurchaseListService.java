package com.bbidoleMarket.bbidoleMarket.api.mypage.service;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dao.PurchaseListDao;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PurchaseListResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.ChatListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseListService {
    private final ChatListRepository chatListRepository;
    private final PurchaseListDao purchaseListDao;


    //구매 목록 조회 <- 최신순인가..?
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
//    private final PurchaseListDao purchaseListDao;
//    public List<PurchaseListResDto> getPurchaseList(Long userId){
//        return purchaseListDao.findPurchaseListByBuyerId(userId);
//    }

    //페이지 네이션
    public PageResDto<PurchaseListResDto> getPurchaseList(Long userId, Integer page, Integer pageSize){
        int totalElements = purchaseListDao.countPurchaseByBuyerId(userId); //전체 페이지 수
        int offset = page*pageSize; //전체 개수?
        List<PurchaseListResDto> content = purchaseListDao.findPurchaseByBuyerId(userId, pageSize, offset);

        int totalPages = (int)Math.ceil((double)totalElements/pageSize);
        boolean last = (page+1)>=totalPages;

        return new PageResDto<>(content, page, pageSize, totalElements, totalPages, last);
    }
}
