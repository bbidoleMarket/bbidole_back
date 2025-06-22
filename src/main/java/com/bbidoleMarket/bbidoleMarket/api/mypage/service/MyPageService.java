package com.bbidoleMarket.bbidoleMarket.api.mypage.service;

import ch.qos.logback.core.spi.ErrorCodes;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.MyPageReqDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.MyPageRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.exception.NotFoundException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageRepository myPageRepository;
    public boolean info(String email) {
        try {
            User user = myPageRepository.findByEmail(email).orElseThrow(NotFoundException::new);
            return true;
        }
        catch (Exception ex) {
            log.info("회원 정보 수정 실패: {}",ex.getMessage());
            return false;
        }

    }
    //회원 정보 수정
    public boolean modifyMyPage(MyPageReqDto myPageReqDto){
        try{
            //유효성 확인
            myPageReqDto.validateUserInfo();
            //이메일로 사용자 찾기
            User user = myPageRepository.findByEmail(myPageReqDto.getEmail())
                    .orElseThrow(()->new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
            //데이터 형식 검증(비번,이메일)
            if(!myPageReqDto.getNickname().matches("^.{2,30}$")) throw new BadRequestException("올바른 닉네임 형식이 아닙니다.");
            if (!myPageReqDto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$]).{8,15}$")) throw new BadRequestException("올바른 비밀번호 형식이 아닙니다.");
            if(!Objects.equals(myPageReqDto.getPasswordConfirm(), myPageReqDto.getPassword())) throw new BadRequestException("비밀번호가 일치하지 않습니다.");

            user.updateNickname(myPageReqDto.getNickname());
            user.updatePassword(myPageReqDto.getPassword());
            return true;
        } catch (Exception e) {
            log.error("회원 정보 수정 실패: {}",e.getMessage());
            return false;
        }
    }
    //프로필 사진 수정
    public boolean modifyProfileImage(MyPageReqDto myPageReqDto){
        try{
            //유효성 검증
            myPageReqDto.validateUserProfileImage();
            String email = myPageReqDto.getEmail();
            String profileImage = myPageReqDto.getProfileImage();

            //이메일로 사용자 찾기
            User user = myPageRepository.findByEmail(myPageReqDto.getEmail())
                    .orElseThrow(()->new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
            //유저의 프로필 이미지 업데이트
            user.updateProfileImage(profileImage);

            return true;
        } catch (Exception e) {
            log.error("프로필 사진 수정 실패: {}",e.getMessage());
            return false;
        }

    }
}
