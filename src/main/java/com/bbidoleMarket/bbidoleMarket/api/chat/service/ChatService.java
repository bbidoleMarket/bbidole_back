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

    public MyChatListDto startChatRoom(ChatRoomReqDto chatRoomReqDto, Long userId) {
        Post post = postRepository.findById(chatRoomReqDto.getPostId()).orElseThrow(
            () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));

        User seller = post.getUser();

        ChatRoom chatRoom = chatRepository.findByPostIdAndBuyerIdAndSellerId(
            post.getId(), userId, seller.getId());

        if (chatRoom != null) {
            return convertToMyChatListDto(chatRoom, userId);
        }

        if (seller.getId().equals(userId)) {
            throw new BadRequestException(ErrorStatus.SELLER_EQUAL_BUY.getMessage());
        }

        User buyer = userRepository.findById(userId).orElseThrow(
            () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        chatRoom = ChatRoom.createChatRoom(post, buyer, seller);
        chatRepository.save(chatRoom);
        return convertToMyChatListDto(chatRoom, userId);
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
    }

    public void setReview(ReviewReqDto reviewReqDto) {
        User buyer = userRepository.findById(reviewReqDto.getBuyerId()).orElseThrow( () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        User seller = userRepository.findById(reviewReqDto.getSellerId()).orElseThrow( () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        Review review = Review.createReview(reviewReqDto.getRating(), reviewReqDto.getContent(), buyer, seller);
        reviewRepository.save(review);
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
        myChatListDto.setCompleted(chatRoom.getIsCompleted());
        if (chatRoom.getBuyer().getId().equals(userId)) {
            myChatListDto.setOthersId(chatRoom.getSeller().getId());
        } else {
            myChatListDto.setOthersId(chatRoom.getBuyer().getId());
        }

        ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(
            chatRoom.getId());
        if (lastMessage != null) {
            myChatListDto.setLastMessage(lastMessage.getContent());
            myChatListDto.setLastMessageSendAt(lastMessage.getSendAt());
        }
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
