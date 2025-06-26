package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostSimpleDto {

    private long postId;
    private String title;
    private int price;
    private String imageUrl;

    public static PostSimpleDto fromPost(Post post) {
        PostSimpleDto postSimpleDto = new PostSimpleDto();
        postSimpleDto.setPostId(post.getId());
        postSimpleDto.setTitle(post.getTitle());
        postSimpleDto.setPrice(post.getPrice());
        postSimpleDto.setImageUrl(post.getImageUrl());
        return postSimpleDto;
    }
}
