package com.bbidoleMarket.bbidoleMarket.api.chat.service;

import com.bbidoleMarket.bbidoleMarket.api.chat.dto.*;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatMessageRepository;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatRepository;
import com.bbidoleMarket.bbidoleMarket.api.entity.*;
import com.bbidoleMarket.bbidoleMarket.api.login.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.PostRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.ReviewRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;
    private final WebSocketService webSocketService;

    public ChatRoomResDto startChatRoom(ChatRoomReqDto chatRoomReqDto, Long userId) {
        Post post = postRepository.findById(chatRoomReqDto.getPostId()).orElseThrow(
            () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));

        User seller = post.getUser();

        ChatRoom chatRoom = chatRepository.findByPostIdAndBuyerIdAndSellerId(
            post.getId(), userId, seller.getId());

        if (chatRoom != null)
            return convertToChatRoomResDto(chatRoom, userId);

        if (seller.getId().equals(userId))
            throw new BadRequestException(ErrorStatus.SELLER_EQUAL_BUY.getMessage());

        User buyer = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        chatRoom = ChatRoom.createChatRoom(post, buyer, seller);
        chatRepository.save(chatRoom);
        return convertToChatRoomResDto(chatRoom, userId);
    }

    public List<MyChatListDto> getMyChatlist(Long userId) {
        List<ChatRoom> chatRooms = chatRepository.findByBuyerIdOrSellerId(userId, userId);
        List<MyChatListDto> myChatListDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            myChatListDtos.add(convertToMyChatListDto(chatRoom, userId));
        }
        // 최신순 정렬
        myChatListDtos.sort(
                Comparator.comparing(
                        MyChatListDto::getLastMessageSendAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        return myChatListDtos;
    }

    public List<ChatMessageResDto> getChatMessages(Long chatId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatId);
        List<ChatMessageResDto> chatMessageResDtos = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            chatMessageResDtos.add(convertToChatMessageResDto(chatMessage));
        }
        return chatMessageResDtos;
    }

    public void setSold(Long userId, Long chatId) {
        ChatRoom chatRoom = chatRepository.findById(chatId).orElseThrow(
            () -> new NotFoundException(ErrorStatus.CHAT_NOT_FOUND_EXCEPTION.getMessage()));
        if (!userId.equals(chatRoom.getSeller().getId())) {
            throw new BadRequestException(ErrorStatus.YOU_ARE_NOT_SELLER.getMessage());
        }
        Post post = chatRoom.getPost();
        post.sold();
        postRepository.save(post);
        chatRoom.completeChat();
        chatRepository.save(chatRoom);

        webSocketService.broadcastSoldEvent(chatId);
    }

    public void setReview(ReviewReqDto reviewReqDto) {
        User buyer = userRepository.findById(reviewReqDto.getBuyerId()).orElseThrow( () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        User seller = userRepository.findById(reviewReqDto.getSellerId()).orElseThrow( () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        Review review = Review.createReview(reviewReqDto.getRating(), reviewReqDto.getContent(), buyer, seller);
        reviewRepository.save(review);
        // 평점 저장
        List<Review> reviews = reviewRepository.findByRevieweeId(seller.getId());
        Double totalRating = reviews.stream().mapToDouble(Review::getRating).average().getAsDouble();
        // 소숫점 첫째자리까지 반올림
        Double rounded = Math.round(totalRating * 10) / 10.0;
        seller.updateTotalRating(rounded);
        userRepository.save(seller);
    }

    private ChatRoomResDto convertToChatRoomResDto(ChatRoom chatRoom, Long userId) {
        ChatRoomResDto chatRoomResDto = new ChatRoomResDto();
        chatRoomResDto.setId(chatRoom.getId());
        chatRoomResDto.setProductId(chatRoom.getPost().getId());
        chatRoomResDto.setProductName(chatRoom.getPost().getTitle());
        chatRoomResDto.setBuyerId(chatRoom.getBuyer().getId());
        chatRoomResDto.setBuyerName(chatRoom.getBuyer().getName());
        chatRoomResDto.setSellerId(chatRoom.getSeller().getId());
        chatRoomResDto.setSellerName(chatRoom.getSeller().getName());
        if (chatRoom.getBuyer().getId().equals(userId))
            chatRoomResDto.setOthersId(chatRoom.getSeller().getId());
        else
            chatRoomResDto.setOthersId(chatRoom.getBuyer().getId());
        chatRoomResDto.setBuyer(chatRoom.getIsCompleted() == true && chatRoom.getBuyer().getId().equals(userId));
        chatRoomResDto.setCompleted(chatRoom.getPost().getIsSold());

        return chatRoomResDto;
    }

    private MyChatListDto convertToMyChatListDto(ChatRoom chatRoom, Long userId) {
        MyChatListDto myChatListDto = new MyChatListDto();
        myChatListDto.setId(chatRoom.getId());
        myChatListDto.setProductId(chatRoom.getPost().getId());
        myChatListDto.setProductName(chatRoom.getPost().getTitle());
        myChatListDto.setSellerId(chatRoom.getSeller().getId());
        myChatListDto.setSellerName(chatRoom.getSeller().getNickname());
        myChatListDto.setBuyerId(chatRoom.getBuyer().getId());
        myChatListDto.setBuyerName(chatRoom.getBuyer().getNickname());
        myChatListDto.setCompleted(chatRoom.getPost().getIsSold());
        if (chatRoom.getBuyer().getId().equals(userId))
            myChatListDto.setOthersId(chatRoom.getSeller().getId());
        else
            myChatListDto.setOthersId(chatRoom.getBuyer().getId());
        myChatListDto.setBuyer(chatRoom.getIsCompleted() == true && chatRoom.getBuyer().getId().equals(userId));
        ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(
            chatRoom.getId());
        if (lastMessage != null) {
            myChatListDto.setLastMessage(lastMessage.getContent());
            myChatListDto.setLastMessageSendAt(lastMessage.getSendAt());
        }
        myChatListDto.setReviewed(chatRoom.getIsReviewed());
        return myChatListDto;
    }

    public ChatMessageResDto convertToChatMessageResDto(ChatMessage chatMessage) {
        ChatMessageResDto chatMessageResDto = new ChatMessageResDto();
        chatMessageResDto.setChatId(chatMessage.getChatRoom().getId());
        chatMessageResDto.setSenderId(chatMessage.getSender().getId());
        chatMessageResDto.setContent(chatMessage.getContent());
        chatMessageResDto.setSendAt(chatMessage.getSendAt());
        return chatMessageResDto;
    }

    public ChatMessage convertToMessageEntity(ChatMessageReqDto chatMessageReqDto) {
        ChatRoom chatRoom = chatRepository.findById(chatMessageReqDto.getChatId()).orElseThrow(
            () -> new NotFoundException(ErrorStatus.CHAT_NOT_FOUND_EXCEPTION.getMessage())
        );
        User sender = userRepository.findById(chatMessageReqDto.getSenderId()).orElseThrow(
            () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage())
        );
        return ChatMessage.createChatMessage(chatMessageReqDto.getContent(), chatRoom, sender);
    }

    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }
}
