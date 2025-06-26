package com.bbidoleMarket.bbidoleMarket.api.mypage.controller;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PurchaseListResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.service.PurchaseListService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/purchased-list")
public class PurchaseListController {
    private final PurchaseListService purchaseListService;

    //구매 목록
//    @GetMapping("/me/purchased-post/{id}") //토큰 검증 추가 시 id제거
//    public ResponseEntity<List<PurchaseListResDto>> getPurchaseList(@PathVariable("id") Long userId){ //@AuthenticationPrincipal UserDetailsImpl user 변경
//        //토큰에서 로그인한 사용자의 id 추출 추가 예정
//
//        return ResponseEntity.ok(purchaseListService.getPurchaseList(userId));
//    }
//    @GetMapping("/me/purchased-post/{id}") //토큰 검증 추가 시 id제거
//    public ResponseEntity<ApiResponse<Void>> getPurchaseList(@PathVariable("id") Long userId){ //@AuthenticationPrincipal UserDetailsImpl user 변경
//        //토큰에서 로그인한 사용자의 id 추출 추가 예정
//
//        purchaseListService.getPurchaseList(userId);
//        return ApiResponse.success_only(SuccessStatus.SEND_HEALTH_SUCCESS);
//    }

    @GetMapping("/me/{id}") //토큰 검증 추가 시 id제거
    public ResponseEntity<ApiResponse<PageResDto<PurchaseListResDto>>> getPurchaseList(@PathVariable("id") Long userId, @RequestParam int page, @RequestParam int pageSize){ //@AuthenticationPrincipal UserDetailsImpl user 변경
        //토큰에서 로그인한 사용자의 id 추출 추가 예정
        //Long userId = userDetails.getUser().getId();
        PageResDto<PurchaseListResDto> result = purchaseListService.getPurchaseList(userId, page, pageSize);
        return ApiResponse.success(SuccessStatus.SEND_HEALTH_SUCCESS,result);
    }
}
