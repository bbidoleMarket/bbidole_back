package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSimpleDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostUpdateReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.service.PostService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "", description = "")
@Slf4j
@RequestMapping("/api/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    @Operation(summary = "게시물 상세 내용 조회입니다.")
    public ResponseEntity<ApiResponse<PostDetailResDto>> findById(@PathVariable Long id) {
        return ApiResponse.success(SuccessStatus.SEARCH_POST_SUCCESS, postService.findById(id));
    }

    // HTTP(Header:Content-Type) 설정 : consumes, produces
    @PutMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시물 수정 입니다.")
    public ResponseEntity<ApiResponse<Void>> update(
        @RequestPart(name = "image", required = false) MultipartFile image,
        @ModelAttribute(name = "dto") PostUpdateReqDto dto,
        @AuthenticationPrincipal String id
    ) {
        postService.update(dto, image,id);
        return ApiResponse.success_only(SuccessStatus.UPDATE_POST_SUCCESS);
    }

    @GetMapping("/seller/{id}")
    @Operation(summary = "판매자의 판매중인 게시물의 조회입니다.")
    public ResponseEntity<ApiResponse<PageResDto<PostSimpleDto>>> findAllBySellerId(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse
            .success(SuccessStatus.SEARCH_POST_SUCCESS, postService.findByUserId(id, page, size));

    }

    @GetMapping("/seller")
    @Operation(summary = "내가 판매하는 게시물의 조회입니다.")
    public ResponseEntity<ApiResponse<PageResDto<PostSimpleDto>>> findAllListByUserId(
            @AuthenticationPrincipal String id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = Long.parseLong(id);
        return ApiResponse
                .success(SuccessStatus.SEARCH_POST_SUCCESS, postService.findByUserId(userId, page, size));

    }



}
