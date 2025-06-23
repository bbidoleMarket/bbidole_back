package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.SellerDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
//    private final PostRepository postRepository;
//    private final ReviewRepository reviewRepository;

    // TODO 삭제
    public Long signin(SigninDto dto) {
        User user = dto.asUser();
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id를 가진 사용자를 찾지 못했습니다."));
    }

    @Transactional(readOnly = true)
    public SellerDetailResDto findSellerById(Long id) {
        User seller = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id를 가진 사용자를 찾지 못했습니다."));
        SellerDetailResDto sellerDetailResDto = new SellerDetailResDto();
        sellerDetailResDto.setUserId(id);
        sellerDetailResDto.setNickname(seller.getNickname());
        sellerDetailResDto.setTotalRating(seller.getTotalRating());
        sellerDetailResDto.setImageUrl(seller.getProfileImage());
        return sellerDetailResDto;
    }

}
