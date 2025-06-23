package com.bbidoleMarket.bbidoleMarket.api.post.controller;

import com.bbidoleMarket.bbidoleMarket.api.post.dto.PageResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostDetailResDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSaveReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostSimpleDto;
import com.bbidoleMarket.bbidoleMarket.api.post.dto.PostUpdateReqDto;
import com.bbidoleMarket.bbidoleMarket.api.post.service.PostService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "", description = "")
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

    // TODO 삭제
    @PostMapping("/")
    public ResponseEntity<ApiResponse<String>> save(@RequestBody PostSaveReqDto dto) {
        postService.save(dto);
        return ApiResponse.success(SuccessStatus.SEARCH_POST_SUCCESS, "게시물 저장에 성공했습니다.");
    }

    @PutMapping("/")
    @Operation(summary = "게시물 수정 입니다.")
    public ResponseEntity<ApiResponse<Void>> update(@RequestBody PostUpdateReqDto dto) {
        postService.update(dto);
        return ApiResponse.success_only(SuccessStatus.UPDATE_POST_SUCCESS);
    }

    @GetMapping("/seller/{id}")
    @Operation(summary = "판매자의 판매중인 게시물의 조회입니다.")
    public ResponseEntity<ApiResponse<PageResDto<PostSimpleDto>>> findAllBySellerId(
        @PathVariable Long id,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return ApiResponse
            .success(SuccessStatus.SEARCH_POST_SUCCESS, postService.findByUserId(id, page, size));

    }


}
