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

    //이름
    @GetMapping("/name")
    public ResponseEntity<ApiResponse<MyPageResDto>> userName(@AuthenticationPrincipal String id){ // @AuthenticationPrincipal
         Long userId = Long.parseLong(id); // 로그인한 사용자 ID

        MyPageResDto dto = myPageService.userName(userId);
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,dto);
    }

    //회원 정보 수정
//    @PutMapping("/update")
//    public ResponseEntity<Boolean> modifyMyPage(@RequestBody MyPageReqDto myPageReqDto) {
//        return ResponseEntity.ok(myPageService.modifyMyPage(myPageReqDto));
//    }
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> modifyMyPage(@RequestBody MyPageReqDto myPageReqDto,@AuthenticationPrincipal String id) {// @AuthenticationPrincipal UserDetailsImpl userDetails
         Long userId = Long.parseLong(id); // 로그인한 사용자 ID

        myPageService.modifyMyPage(myPageReqDto,userId); //이메일로 검증 중 변경 예정
        return ApiResponse.success_only(SuccessStatus.SEND_USER_UPDATE_SUCCESS);
    }
    //프로필 사진 수정
//    @PutMapping("/profile-image")
//    public ResponseEntity<Boolean> modifyProfileImage(@RequestBody MyPageReqDto myPageReqDto){
//        return ResponseEntity.ok(myPageService.modifyProfileImage(myPageReqDto));
//    }


    @PutMapping("/profile-image")
    public ResponseEntity<ApiResponse<Void>> modifyProfileImage(@ModelAttribute MyPageReqDto myPageReqDto,@AuthenticationPrincipal String id ,@RequestParam MultipartFile image) {// @AuthenticationPrincipal UserDetailsImpl userDetails
        //토큰 검증
        Long userId = Long.parseLong(id); //토큰에서 id추출

        myPageService.modifyProfileImage(myPageReqDto,image,userId); //이메일로 검증 중 변경예정
        return ApiResponse.success_only(SuccessStatus.SEND_USER_UPDATE_SUCCESS);
    }
}
