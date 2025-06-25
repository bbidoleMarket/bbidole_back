package com.bbidoleMarket.bbidoleMarket.api.createpost.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private Integer price;
    private String imageUrl;
    private String description;
    private String userNickname;

    /**
     * Post 엔티티를 응답 DTO로 변환
     */
    public static PostResponseDto fromEntity(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .imageUrl(post.getImageUrl())
                .description(post.getDescription())
                .userNickname(post.getUser().getNickname())
                .build();
    }
}