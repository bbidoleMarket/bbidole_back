package com.bbidoleMarket.bbidoleMarket.api.chat.service;

import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageReqDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageResDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatRoomReqDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.MyChatListDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatMessageRepository;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatRepository;
import com.bbidoleMarket.bbidoleMarket.api.entity.ChatMessage;
import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.user.UserRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public MyChatListDto startChatRoom(ChatRoomReqDto chatRoomReqDto, Long userId) {
        ChatRoom chatRoom = chatRepository.findByPostIdAndBuyerIdAndSellerId(
                chatRoomReqDto.getPostId(), chatRoomReqDto.getBuyerId(), chatRoomReqDto.getSellerId());

        if (chatRoom != null)
            return convertToMyChatListDto(chatRoom, userId);

        Post post = postRepository.findById(chatRoomReqDto.getPostId()).orElseThrow(
                () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));

        User seller = post.getUser();
        User buyer = userRepository.findById(chatRoomReqDto.getBuyerId()).orElseThrow(
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

    public void setSold(Long id) {
        ChatRoom chatRoom = chatRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorStatus.CHAT_NOT_FOUND_EXCEPTION.getMessage()));
        chatRoom.completeChat();
        chatRepository.save(chatRoom);
    }

    private MyChatListDto convertToMyChatListDto(ChatRoom chatRoom, Long userId) {
        MyChatListDto myChatListDto = new MyChatListDto();
        myChatListDto.setId(chatRoom.getId());
        myChatListDto.setProductId(chatRoom.getPost().getId());
        myChatListDto.setProductName(chatRoom.getPost().getTitle());
        myChatListDto.setSellerId(chatRoom.getSeller().getId());
        myChatListDto.setSellerName(chatRoom.getSeller().getName());
        myChatListDto.setBuyerId(chatRoom.getBuyer().getId());
        myChatListDto.setBuyerName(chatRoom.getBuyer().getName());
        myChatListDto.setCompleted(chatRoom.getIsCompleted());
        if (chatRoom.getBuyer().getId().equals(userId)) {
            myChatListDto.setOthersId(chatRoom.getSeller().getId());
        } else
            myChatListDto.setOthersId(chatRoom.getBuyer().getId());
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
