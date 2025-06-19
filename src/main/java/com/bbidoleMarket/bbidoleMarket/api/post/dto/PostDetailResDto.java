package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import com.bbidoleMarket.bbidoleMarket.api.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostDetailResDto {

    private long postId;
    private String title;
    private int price;
    private String description;
    private String imageUrl;

    private long writerId;
    private String writerNickname;
    private String writerImageUrl;
    private Double writerTotalRating;

    public static PostDetailResDto fromPost(Post post) {
        User writer = post.getUser();
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setPostId(post.getId());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setPrice(post.getPrice());
        postDetailResDto.setDescription(post.getDescription());
        postDetailResDto.setImageUrl(post.getImageUrl());
        postDetailResDto.setWriterId(writer.getId());
        postDetailResDto.setWriterNickname(writer.getNickname());
        postDetailResDto.setWriterImageUrl(writer.getProfileImage());
        postDetailResDto.setWriterTotalRating(writer.getTotalRating());
        return postDetailResDto;
    }
}
