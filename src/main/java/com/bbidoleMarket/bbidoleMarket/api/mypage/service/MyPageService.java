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

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageRepository myPageRepository;

    //회원 정보 수정
    public boolean modifyMyPage(MyPageReqDto myPageReqDto){
        try{
            //입력값 검증을 어디서 service? dto?
            myPageReqDto.validate();
            //해당 사용자 확인 해야하나?
            User user = myPageRepository.findByEmail(myPageReqDto.getEmail())
                    .orElseThrow(()->new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
            //데이터 형식 검증 추가(비번,이메일)
            //if(!myPageReqDto.getNickname().matches()) throw new BadRequestException(ErrorStatus.VALIDATION_EMAIL_FORMAT_EXCEPTION.getMessage());
            //if(myPageReqDto.getPassword().length()<8 ||!myPageReqDto.getPassword().matches()) throw new BadRequestException("올바른 비밀번호 형식이 아닙니다.");
            //if(myPageReqDto.getPasswordConfirm()!=myPageReqDto.getPassword() ) throw new BadRequestException("비밀번호가 일치하지 않습니다.");

            user.updateNickname(myPageReqDto.getNickname());
            user.updatePassword(myPageReqDto.getPassword());
            return true;
        } catch (Exception e) {
            log.error("회원 정보 수정 실패: {}",e.getMessage());
            return false;
        }
    }
    //프로필 사진 수정
    public boolean modifyProfileImage(String profileImage){
        try{
            User user = myPageRepository.findByProfileImage(profileImage).orElseThrow(
                    ()-> new BadRequestException("사진이 존재하지 않습니다."));
            user.updateProfileImage(profileImage);
            return true;
        } catch (Exception e) {
            log.error("프로필 사진 수정 실패: {}",e.getMessage());
            return false;
        }

    }
}
