package com.bbidoleMarket.bbidoleMarket.api.mypage.controller;

import com.bbidoleMarket.bbidoleMarket.api.mypage.dto.PurchaseListResDto;
import com.bbidoleMarket.bbidoleMarket.api.mypage.service.PurchaseListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class PurchaseListController {
    private final PurchaseListService purchaseListService;

    @GetMapping("/me/purchased-post/{id}") //토큰 검증 추가 시 id제거
    public ResponseEntity<List<PurchaseListResDto>> getPurchaseList(@PathVariable("id") Long userId){ //@AuthenticationPrincipal UserDetailsImpl user 변경
        //토큰에서 로그인한 사용자의 id 추출 추가 예정

        return ResponseEntity.ok(purchaseListService.getPurchaseList(userId));
    }
}
