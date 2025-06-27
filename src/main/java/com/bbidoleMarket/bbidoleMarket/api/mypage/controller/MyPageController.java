package com.bbidoleMarket.bbidoleMarket.api.mypage.controller;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.MyPageReqDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.MyPageResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.MyPageRepository;
import com.bbidoleMarket.bbidoleMarket.api.mypage.service.MyPageService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RequestMapping("/api/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    //프로필이미지,이름
    @GetMapping("/info-update")
    public ResponseEntity<ApiResponse<MyPageResDto>> userInfo(@AuthenticationPrincipal String id){
        Long userId = Long.parseLong(id);
        MyPageResDto dto = myPageService.userProfile(userId);
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,dto);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> modifyMyPage(@AuthenticationPrincipal String id, @RequestBody MyPageReqDto myPageReqDto) {// @AuthenticationPrincipal UserDetailsImpl userDetails
        Long userId = Long.parseLong(id);
        myPageService.modifyMyPage(userId,myPageReqDto);
        return ApiResponse.success_only(SuccessStatus.SEND_USER_UPDATE_SUCCESS);
    }

    @PutMapping("/profile-image")
    public ResponseEntity<ApiResponse<Void>> modifyProfileImage(@AuthenticationPrincipal String id, @RequestParam MultipartFile image) {
        Long userId = Long.parseLong(id);
        myPageService.modifyProfileImage(userId, image);
        return ApiResponse.success_only(SuccessStatus.SEND_USER_UPDATE_SUCCESS);
    }
}
