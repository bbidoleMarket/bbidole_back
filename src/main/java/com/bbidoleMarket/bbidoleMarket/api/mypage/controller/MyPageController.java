package com.bbidoleMarket.bbidoleMarket.api.mypage.controller;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.MyPageReqDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.repository.MyPageRepository;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageRepository myPageRepository;

    //회원 정보 수정
    public ResponseEntity<ApiResponse<Void>> modifyMyPage(MyPageReqDto myPageReqDto) {

        return ApiResponse.success_only(SuccessStatus.SEND_HEALTH_SUCCESS);
    }
}
