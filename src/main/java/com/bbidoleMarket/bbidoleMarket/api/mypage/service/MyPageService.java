package com.bbidoleMarket.bbidoleMarket.api.mypage.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import com.bbidoleMarket.bbidoleMarket.api.image.enums.ImageFolder;
import com.bbidoleMarket.bbidoleMarket.api.image.service.UploadImageService;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.MyPageReqDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.MyPageResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.MyPageRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageRepository myPageRepository;
    private final UploadImageService uploadImageService;
    private final PasswordEncoder passwordEncoder;

    //이름,프로필이미지 출력
    public MyPageResDto userProfile(String email){
        //이메일로 사용자 찾기 변경예정
        User user = myPageRepository.findByEmail(email)
                .orElseThrow(()->new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        MyPageResDto myPageResDto = new MyPageResDto();
        myPageResDto.setProfileImage(user.getProfileImage());
        myPageResDto.setName(user.getName());
        return myPageResDto;
    }

    //회원 정보 수정
    public void modifyMyPage(MyPageReqDto myPageReqDto){ //Long userId
        //유효성 확인
        myPageReqDto.validateUserInfo();
        //이메일로 사용자 찾기 변경예정
        User user = myPageRepository.findByEmail(myPageReqDto.getEmail())
                .orElseThrow(()->new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        //토큰에서 추출한 id넘겨 받으면 검증
        //User user = myPageRepository.findById(userId)
        //        .orElseThrow(new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        //데이터 형식 검증(비번,이메일)
        if(!myPageReqDto.getNickname().matches("^.{2,30}$")) throw new BadRequestException("올바른 닉네임 형식이 아닙니다.");
        if (!myPageReqDto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$]).{8,15}$")) throw new BadRequestException("올바른 비밀번호 형식이 아닙니다.");
        if(!Objects.equals(myPageReqDto.getPasswordConfirm(), myPageReqDto.getPassword())) throw new BadRequestException("비밀번호가 일치하지 않습니다.");

        user.updateNickname(myPageReqDto.getNickname());
        user.updatePassword(myPageReqDto.getPassword(),passwordEncoder);

    }
    //프로필 사진 수정
    public void modifyProfileImage(MyPageReqDto myPageReqDto, MultipartFile image) { //Long userId

    //기본 이미지로 바꾸기?

        //사용자 이메일 받아서 회원 확인 -> 토큰으로 변경 예정
        String email = myPageReqDto.getEmail();
        //이메일로 사용자 찾기
        User user = myPageRepository.findByEmail(myPageReqDto.getEmail())
                .orElseThrow(()->new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));
        //토큰에서 추출한 id넘겨 받으면 검증
        //User user = myPageRepository.findById(userId)
        //        .orElseThrow(new BadRequestException(ErrorStatus.USER_NOT_FOUND_EXCEPTION.getMessage()));

        String profileImage;
        try {
            profileImage = uploadImageService.uploadImage(image, ImageFolder.PROFILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //유저의 프로필 이미지 업데이트
        user.updateProfileImage(profileImage);

    }
}
