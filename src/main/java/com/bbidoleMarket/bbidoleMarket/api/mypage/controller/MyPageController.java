package com.bbidoleMarket.bbidoleMarket.api.mypage.controller;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.MyPageReqDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.MyPageRepository;
import com.bbidoleMarket.bbidoleMarket.api.mypage.service.MyPageService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RequestMapping("/api/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    //회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<Boolean> modifyMyPage(@RequestBody MyPageReqDto myPageReqDto) {
        return ResponseEntity.ok(myPageService.modifyMyPage(myPageReqDto));
    }
    //프로필 사진 수정
    @PutMapping("/profile-image")
    public ResponseEntity<Boolean> modifyProfileImage(@RequestBody MyPageReqDto myPageReqDto){
        return ResponseEntity.ok(myPageService.modifyProfileImage(myPageReqDto));
    }
}
