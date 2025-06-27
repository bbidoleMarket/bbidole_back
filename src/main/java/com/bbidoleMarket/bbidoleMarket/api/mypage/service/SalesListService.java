package com.bbidoleMarket.bbidoleMarket.api.mypage.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.SalesListResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.ChatListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalesListService {
    private final ChatListRepository chatListRepository;

    //판매 목록 조회
    //최신순
    @Transactional(readOnly = true)
    public PageResDto<SalesListResDto> getLatestSales(Long userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        System.out.println(111);
        //판매 중인 글만 페이징 조회
        Page<ChatRoom> chatsOnSale = chatListRepository.findBySellerIdOrderByCreatedAtDesc(userId,pageRequest);
        System.out.println(222);
        //DTO로 변환
        Page<SalesListResDto> salesListResDto = chatsOnSale.map(chat -> convertEntityToDto(chat.getPost()));
        System.out.println(333);
        return new PageResDto<>(salesListResDto);
    }
    //판매 중
    @Transactional(readOnly = true)
    public PageResDto<SalesListResDto> getOnSalesList(Long userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        //판매 중인 글만 페이징 조회
        Page<ChatRoom> chatsOnSale = chatListRepository.findBySellerIdAndPost_IsSold(userId,false,pageRequest);
        //DTO로 변환
        Page<SalesListResDto> salesListResDto = chatsOnSale.map(chat -> convertEntityToDto(chat.getPost()));
        return new PageResDto<>(
                salesListResDto.getContent(),
                salesListResDto.getNumber(),
                salesListResDto.getSize(),
                salesListResDto.getTotalElements(),
                salesListResDto.getTotalPages(),
                salesListResDto.isLast()
        );
    }
    //판매 완료
    @Transactional(readOnly = true)
    public PageResDto<SalesListResDto> getCompletedSalesList(Long userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        //판매 중인 글만 페이징 조회
        Page<ChatRoom> chatsOnSale = chatListRepository.findBySellerIdAndIsCompleted(userId,true,pageRequest);
        //DTO로 변환
        Page<SalesListResDto> salesListResDto = chatsOnSale.map(chat -> convertEntityToDto(chat.getPost()));
        return new PageResDto<>(salesListResDto);
    }

//        //페이지네이션
//    @Transactional(readOnly = true)
//    public PageResDto<SalesListResDto> getSalesList(Long userId, Integer page, Integer pageSize){
//        //최신순
//        List<ChatRoom> chatsDate = chatListRepository.findBySellerId(userId);
//        //판매완료
//        List<ChatRoom> chatsCompletedSale = chatListRepository.findBySellerIdAndIsCompleted(userId,true);
//        //판매중
//        List<ChatRoom> chatsOnSale = chatListRepository.findBySellerIdAndIsSold(userId,false);
//
//    }
    //Entity -> DTO
    private SalesListResDto convertEntityToDto(Post post){
        SalesListResDto salesListResDto = new SalesListResDto();
        salesListResDto.setTitle(post.getTitle());
        salesListResDto.setPrice(post.getPrice());
        salesListResDto.setImageUrl(post.getImageUrl());
        salesListResDto.setRegDate(post.getCreatedAt());
        return salesListResDto;
    }
}
