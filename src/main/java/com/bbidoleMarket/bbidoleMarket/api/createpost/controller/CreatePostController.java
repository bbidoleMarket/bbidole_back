package com.bbidoleMarket.bbidoleMarket.api.createpost.controller;

import com.bbidoleMarket.bbidoleMarket.api.createpost.service.CreatePostService;
import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.createpost.dto.CreatePostDto;
import com.bbidoleMarket.bbidoleMarket.api.createpost.dto.PostResponseDto;

import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/createpost")
public class CreatePostController {

    private final CreatePostService createpostService;

    /**
     * 게시글 작성 API
     * 현재는 인증 기능 없이 임시 사용자로 테스트
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String price,
            @RequestParam MultipartFile image,
            @AuthenticationPrincipal String id
    ) {

        // 수동으로 DTO 생성 및 타입 변환
        Integer priceValue = null;
        try {
            priceValue = Integer.parseInt(price);
        } catch (NumberFormatException e) {
            throw new BadRequestException("가격은 숫자만 입력 가능합니다.");
        }

        CreatePostDto dto = CreatePostDto.builder()
                .title(title)
                .description(description)
                .price(priceValue)
                .build();


        // 게시글 생성
        Post createdPost = createpostService.createPost(dto, image, id);

        // 응답 DTO 변환
        PostResponseDto responseDto = PostResponseDto.fromEntity(createdPost);

        return ApiResponse.success(SuccessStatus.CREATE_POST_SUCCESS, responseDto);
    }
}