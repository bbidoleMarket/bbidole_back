package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResDto {

    private long postId;
    private String title;
    private int price;
    private String description;
    private String imageUrl;
    private boolean isSold;
    private boolean isDeleted; // 이 필드 추가
    private LocalDateTime createdAt;

    private boolean isWriter;

    private long writerId;
    private String writerNickname;
    private String writerImageUrl;
    private Double writerTotalRating;

    public static PostDetailResDto fromPost(Post post, boolean isWriter) {
        User writer = post.getUser();
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setPostId(post.getId());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setPrice(post.getPrice());
        postDetailResDto.setDescription(post.getDescription());
        postDetailResDto.setImageUrl(post.getImageUrl());
        postDetailResDto.setSold(post.getIsSold());
        postDetailResDto.setDeleted(post.getIsDeleted()); // 이 필드 추가
        postDetailResDto.setCreatedAt(post.getCreatedAt());

        postDetailResDto.isWriter = isWriter;

        postDetailResDto.setWriterId(writer.getId());
        postDetailResDto.setWriterNickname(writer.getNickname());
        postDetailResDto.setWriterImageUrl(writer.getProfileImage());
        postDetailResDto.setWriterTotalRating(writer.getTotalRating());
        return postDetailResDto;
    }
}
