package com.bbidoleMarket.bbidoleMarket.api.mypage.controller;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.SalesListResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.service.SalesListService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/sales-list")
public class SalesListController {
    private final SalesListService salesListService;

    //최신순
    @GetMapping("/me/latest")//토큰 검증 추가 시 id제거
    public ResponseEntity<ApiResponse<PageResDto<SalesListResDto>>> getLatestList(@RequestParam int page, @RequestParam int pageSize, @AuthenticationPrincipal String id){
        //토큰에서 로그인한 사용자의 id 추출 추가 예정
        System.out.println("getLatestList");

        Long userId = Long.parseLong(id);
        PageResDto<SalesListResDto> result = salesListService.getLatestSales(userId, page, pageSize);
        System.out.println("getLatestList2222");
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,result);
    }

    //판매중
    @GetMapping("/me/on")//토큰 검증 추가 시 id제거
    public ResponseEntity<ApiResponse<PageResDto<SalesListResDto>>> getOnSalesList(@AuthenticationPrincipal String id, @RequestParam int page, @RequestParam int pageSize){
        //토큰에서 로그인한 사용자의 id 추출 추가 예정
        Long userId = Long.parseLong(id);
        PageResDto<SalesListResDto> result = salesListService.getOnSalesList(userId, page, pageSize);
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,result);
    }

    //판매완료
    @GetMapping("/me/completed")//토큰 검증 추가 시 id제거
    public ResponseEntity<ApiResponse<PageResDto<SalesListResDto>>> getCompletedSalesList(@AuthenticationPrincipal String id, @RequestParam int page, @RequestParam int pageSize){ //@AuthenticationPrincipal UserDetailsImpl userDetails
        //토큰에서 로그인한 사용자의 id 추출 추가 예정
        Long userId = Long.parseLong(id);
        PageResDto<SalesListResDto> result = salesListService.getCompletedSalesList(userId, page, pageSize);
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,result);
    }
}
