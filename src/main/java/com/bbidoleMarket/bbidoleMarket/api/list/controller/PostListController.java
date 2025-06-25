package com.bbidoleMarket.bbidoleMarket.api.list.controller;

import com.bbidoleMarket.bbidoleMarket.api.list.dto.PostListDto;
import com.bbidoleMarket.bbidoleMarket.api.list.dto.PostSearchCondition;
import com.bbidoleMarket.bbidoleMarket.api.list.service.PostListService;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ApiResponse;
import com.bbidoleMarket.bbidoleMarket.common.reponse.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/posts")
public class PostListController {

    private final PostListService postListService;

    /**
     * 물품 목록 조회 API
     * @param keyword 검색어 (제목 또는 내용)
     * @param onlySelling 판매중인 상품만 조회 여부
     * @param sort 정렬 기준 (latest: 최신순, priceAsc: 가격낮은순, priceDesc: 가격높은순)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 물품 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostListDto>>> getPostList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "true") boolean onlySelling,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PostSearchCondition condition = PostSearchCondition.builder()
                .keyword(keyword)
                .onlySelling(onlySelling)
                .sort(sort)
                .page(page)
                .size(size)
                .build();

        Page<PostListDto> result = postListService.getPostList(condition);
        return ApiResponse.success(SuccessStatus.GET_POST_LIST_SUCCESS, result);
    }

    /**
     * 물품 실시간 검색 API (검색어 입력 중에 호출)
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PostListDto>>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "true") boolean onlySelling,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 실시간 검색은 결과 수를 적게 하여 빠르게 응답
        PostSearchCondition condition = PostSearchCondition.builder()
                .keyword(keyword)
                .onlySelling(onlySelling)
                .sort(sort)
                .page(page)
                .size(size)
                .build();

        Page<PostListDto> result = postListService.getPostList(condition);
        return ApiResponse.success(SuccessStatus.SEARCH_POST_SUCCESS, result);
    }
}