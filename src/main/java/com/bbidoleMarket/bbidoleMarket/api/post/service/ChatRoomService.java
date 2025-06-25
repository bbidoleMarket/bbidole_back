package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.ChatRoom;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.login.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.ChatRoomReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.ChatRoomResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.ChatRoomRepository;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.PostRepository;

import com.bbidoleMarket.bbidoleMarket.common.exception.InternalServerException;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ChatRoomResDto findById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(NotFoundException::new);
        ChatRoomResDto chatRoomResDto = new ChatRoomResDto();
        chatRoomResDto.setProductId(chatRoom.getPost().getId());
        chatRoomResDto.setBuyerId(chatRoom.getBuyer().getId());
        chatRoomResDto.setSellerId(chatRoom.getSeller().getId());
        return chatRoomResDto;
    }


    public Long startChatRoom(ChatRoomReqDto dto) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByPostIdAndBuyerIdAndSellerIdAndIsCompleted(
            dto.getProductId(), dto.getBuyerId(), dto.getSellerId(), false);

        if (chatRooms.size() > 1) {
            throw new InternalServerException(
                ErrorStatus.DUPLICATE_CHAT_ROOM_CREATE_EXCEPTION.getMessage());
        }
        if (chatRooms.size() == 1) {
            return chatRooms.get(0).getId();
        }

        Post post = postRepository.findById(dto.getProductId()).orElseThrow(
            () -> new NotFoundException(ErrorStatus.POST_NOT_FOUND_EXCEPTION.getMessage()));

        User seller = post.getUser();
        User buyer = userRepository.findById(dto.getBuyerId()).orElseThrow(
            () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        ChatRoom chatRoom = ChatRoom.createChatRoom(post, buyer, seller);
        return chatRoomRepository.save(chatRoom).getId();
    }
}
