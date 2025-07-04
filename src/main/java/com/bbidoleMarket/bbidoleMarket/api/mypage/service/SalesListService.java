package com.bbidoleMarket.bbidoleMarket.api.mypage.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.SalesListResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.BuySellRepository;
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
    private final BuySellRepository buySellRepository;

    //판매 목록 조회
    //최신순
    @Transactional(readOnly = true)
    public PageResDto<SalesListResDto> getLatestSales(Long userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        //판매 중인 글만 페이징 조회
        Page<Post> onSaleLatest = buySellRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId,pageRequest);
        //DTO로 변환
        Page<SalesListResDto> salesListResDto = onSaleLatest.map(post-> convertEntityToDto(post));
        return new PageResDto<>(salesListResDto);
    }

    //판매 목록 조회
    //최신순
//    @Transactional(readOnly = true)
//    public PageResDto<SalesListResDto> getLatestSales(Long userId, Integer page, Integer pageSize) {
//        PageRequest pageRequest = PageRequest.of(page, pageSize);
//        //판매 중인 글만 페이징 조회
//        Page<ChatRoom> chatsOnSale = chatListRepository.findBySellerIdOrderByCreatedAtDesc(userId,pageRequest);
//        //DTO로 변환
//        Page<SalesListResDto> salesListResDto = chatsOnSale.map(chat -> convertEntityToDto(chat.getPost()));
//        return new PageResDto<>(salesListResDto);
//    }
    //판매 중
    @Transactional(readOnly = true)
    public PageResDto<SalesListResDto> getOnSalesList(Long userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        //판매 중인 글만 페이징 조회
        Page<Post> onSale = buySellRepository.findByUserIdAndIsSoldAndIsDeletedFalseOrderByCreatedAtDesc(userId,false,pageRequest);
        //DTO로 변환
        Page<SalesListResDto> salesListResDto = onSale.map(post -> convertEntityToDto(post));
        return new PageResDto<>(salesListResDto);
    }
//    @Transactional(readOnly = true)
//    public PageResDto<SalesListResDto> getCompletedSalesList(Long userId, Integer page, Integer pageSize) {
//        PageRequest pageRequest = PageRequest.of(page, pageSize);
//        //판매 중인 글만 페이징 조회
//        Page<ChatRoom> chatsOnSale = chatListRepository.findBySellerIdAndIsCompleted(userId,true,pageRequest);
//        //DTO로 변환
//        Page<SalesListResDto> salesListResDto = chatsOnSale.map(chat -> convertEntityToDto(chat.getPost()));
//        return new PageResDto<>(salesListResDto);
//    }
    //판매 완료

    @Transactional(readOnly = true)
    public PageResDto<SalesListResDto> getCompletedSalesList(Long userId, Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        //판매 중인 글만 페이징 조회
        Page<Post> postList = buySellRepository.findByUserIdAndIsSoldAndIsDeletedFalseOrderByCreatedAtDesc(userId,true,pageRequest);
        //DTO로 변환
        Page<SalesListResDto> salesListResDto = postList.map(post -> convertEntityToDto(post));
        return new PageResDto<>(salesListResDto);
    }

//    @Transactional(readOnly = true)
//    public PageResDto<SalesListResDto> getCompletedSalesList(Long userId, Integer page, Integer pageSize) {
//        PageRequest pageRequest = PageRequest.of(page, pageSize);
//        //판매 중인 글만 페이징 조회
//        Page<ChatRoom> chatsOnSale = chatListRepository.findBySellerIdAndIsCompleted(userId,true,pageRequest);
//        //DTO로 변환
//        Page<SalesListResDto> salesListResDto = chatsOnSale.map(chat -> convertEntityToDto(chat.getPost()));
//        return new PageResDto<>(salesListResDto);
//    }

    //Entity -> DTO
    private SalesListResDto convertEntityToDto(Post post){
        SalesListResDto salesListResDto = new SalesListResDto();
        salesListResDto.setTitle(post.getTitle());
        salesListResDto.setPrice(post.getPrice());
        salesListResDto.setImageUrl(post.getImageUrl());
        salesListResDto.setRegDate(post.getCreatedAt());
        salesListResDto.setPostId(post.getId());
        return salesListResDto;
    }
}
