package com.bbidoleMarket.bbidoleMarket.api.chat.service;

import com.bbidoleMarket.bbidoleMarket.api.chat.dto.ChatMessageDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.dto.MyChatListDto;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatMessageRepository;
import com.bbidoleMarket.bbidoleMarket.api.chat.repository.ChatRepository;
import com.bbidoleMarket.bbidoleMarket.api.entity.ChatMessage;
import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<MyChatListDto> getMyChatlist(Long userId) {
        List<ChatRoom> chatRooms = chatRepository.findByBuyerIdOrSellerId(userId, userId);
        List<MyChatListDto> myChatListDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            myChatListDtos.add(convertToMyChatListDto(chatRoom));
        }
        return myChatListDtos;
    }

    public List<ChatMessageDto> getChatMessages(Long chatId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatId);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            chatMessageDtos.add(convertToChatMessageDto(chatMessage));
        }
        return chatMessageDtos;
    }

    public void setSold(Long id) {
        ChatRoom chatRoom = chatRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorStatus.CHAT_NOT_FOUND_EXCEPTION.getMessage()));
        chatRoom.completeChat();
        chatRepository.save(chatRoom);
    }

    private MyChatListDto convertToMyChatListDto(ChatRoom chatRoom) {
        MyChatListDto myChatListDto = new MyChatListDto();
        myChatListDto.setChatId(chatRoom.getId());
        myChatListDto.setProductName(chatRoom.getPost().getTitle());
        myChatListDto.setSellerName(chatRoom.getSeller().getName());
        myChatListDto.setBuyerName(chatRoom.getBuyer().getName());
        myChatListDto.setCompleted(chatRoom.getIsCompleted());
        return myChatListDto;
    }

    private ChatMessageDto convertToChatMessageDto(ChatMessage chatMessage) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setChatId(chatMessage.getChatRoom().getId());
        chatMessageDto.setSenderId(chatMessage.getSender().getId());
        chatMessageDto.setContent(chatMessage.getContent());
        return chatMessageDto;
    }
}
