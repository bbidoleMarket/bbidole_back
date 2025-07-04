package com.bbidoleMarket.bbidoleMarket.api.list.service;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.list.dto.PostListDto;
import com.bbidoleMarket.bbidoleMarket.api.list.dto.PostSearchCondition;
import com.bbidoleMarket.bbidoleMarket.api.list.repository.PostListRepository;
import com.bbidoleMarket.bbidoleMarket.common.exception.BadRequestException;
import com.bbidoleMarket.bbidoleMarket.common.reponse.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostListService {

    private final PostListRepository postListRepository;

    /**
     * 물품 목록 조회 (페이지네이션 및 정렬 적용)
     */
    public Page<PostListDto> getPostList(PostSearchCondition condition) {
        if (condition.getPage() < 0 || condition.getSize() <= 0) {
            throw new BadRequestException(ErrorStatus.VALIDATION_REQUEST_PAGENATION_EXCEPTION.getMessage());
        }

        Pageable pageable = PageRequest.of(condition.getPage(), condition.getSize());
        String keyword = condition.getKeyword();
        String sort = condition.getSort() != null ? condition.getSort() : "latest";

        // 키워드가 없는 경우
        if (keyword == null || keyword.isBlank()) {
            return getPostsWithoutKeyword(condition.getOnlySelling(), sort, pageable);
        }

        // 검색 조건이 있는 경우
        return getPostsWithKeyword(keyword, condition.getOnlySelling(), sort, pageable);
    }

    /**
     * 키워드 없이 물품 목록 조회
     */
    private Page<PostListDto> getPostsWithoutKeyword(Boolean onlySelling, String sort, Pageable pageable) {
        if (Boolean.TRUE.equals(onlySelling)) {
            // 메서드 이름 수정 필요
            return switch (sort) {
                case "priceAsc" -> postListRepository.findByIsSoldAndIsDeletedFalseOrderByPriceAsc(false, pageable)
                        .map(this::convertToDto);
                case "priceDesc" -> postListRepository.findByIsSoldAndIsDeletedFalseOrderByPriceDesc(false, pageable)
                        .map(this::convertToDto);
                default -> postListRepository.findByIsSoldAndIsDeletedFalseOrderByCreatedAtDesc(false, pageable)
                        .map(this::convertToDto);
            };
        } else {
            // 메서드 이름 수정 필요
            return switch (sort) {
                case "priceAsc" -> postListRepository.findByIsDeletedFalseOrderByPriceAsc(pageable)
                        .map(this::convertToDto);
                case "priceDesc" -> postListRepository.findByIsDeletedFalseOrderByPriceDesc(pageable)
                        .map(this::convertToDto);
                default -> postListRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable)
                        .map(this::convertToDto);
            };
        }
    }

    /**
     * 키워드로 물품 검색
     */
    private Page<PostListDto> getPostsWithKeyword(String keyword, Boolean onlySelling, String sort, Pageable pageable) {
        return switch (sort) {
            case "priceAsc" -> postListRepository.searchPostsByPriceAsc(keyword, onlySelling, pageable)
                    .map(this::convertToDto);
            case "priceDesc" -> postListRepository.searchPostsByPriceDesc(keyword, onlySelling, pageable)
                    .map(this::convertToDto);
            default -> postListRepository.searchPostsByLatest(keyword, onlySelling, pageable)
                    .map(this::convertToDto);
        };
    }

    /**
     * Entity를 DTO로 변환
     */
    private PostListDto convertToDto(Post post) {
        return PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .isSold(post.getIsSold())
                .build();
    }
}