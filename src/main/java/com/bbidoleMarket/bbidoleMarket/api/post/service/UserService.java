package com.bbidoleMarket.bbidoleMarket.api.post.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.SellerDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.SigninDto;
import com.bbidoleMarket.bbidoleMarket.api.post.repository.UserRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    // TODO 삭제
    @Transactional
    public Long signin(SigninDto dto) {
        User user = dto.asUser();
        return userRepository.save(user).getId();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id를 가진 사용자를 찾을 수 없습니다."));
    }

    public SellerDetailResDto findSellerById(Long id) {
        User seller = userRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        SellerDetailResDto sellerDetailResDto = new SellerDetailResDto();
        sellerDetailResDto.setUserId(id);
        sellerDetailResDto.setNickname(seller.getNickname());
        sellerDetailResDto.setTotalRating(seller.getTotalRating());
        sellerDetailResDto.setImageUrl(seller.getProfileImage());
        return sellerDetailResDto;
    }

}
