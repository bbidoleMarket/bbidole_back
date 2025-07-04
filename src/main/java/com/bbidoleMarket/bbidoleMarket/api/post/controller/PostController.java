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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "게시물", description = "게시물에 관련된 API")
@Slf4j
@RequestMapping("/api/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    @Operation(summary = "게시물 상세 내용 조회입니다.")
    public ResponseEntity<ApiResponse<PostDetailResDto>> findById(
        @PathVariable Long postId,
        @AuthenticationPrincipal String jwtId
    ) {
        return ApiResponse.success(SuccessStatus.SEARCH_POST_SUCCESS,
            postService.findById(postId, jwtId));
    }

    // HTTP (Header:Content-Type) 설정: consumes, produces
    @PutMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시물 수정 입니다.")
    public ResponseEntity<ApiResponse<Void>> update(
        @RequestPart(name = "image", required = false) MultipartFile image,
        @ModelAttribute(name = "dto") PostUpdateReqDto dto,
        @AuthenticationPrincipal String id
    ) {
        postService.update(dto, image, id);
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

    @GetMapping("/my")
    @Operation(summary = "내가 판매하는 게시물의 조회입니다.")
    public ResponseEntity<ApiResponse<PageResDto<PostSimpleDto>>> findAllListByUserId(
        @AuthenticationPrincipal String id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Long userId = Long.parseLong(id);
        return ApiResponse
            .success(SuccessStatus.SEARCH_POST_SUCCESS,
                postService.findByUserId(userId, page, size));
    }

    @PostMapping("/{postId}")
    @Operation(summary = "게시물 삭제")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal String id) {

        postService.softDeletePost(postId, id);

        return ApiResponse.success_only(SuccessStatus.DELETE_POST_SUCCESS);
    }
}
