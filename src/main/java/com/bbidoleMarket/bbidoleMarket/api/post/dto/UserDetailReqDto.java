package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDetailReqDto {

    private long userId;
    private String nickname;
    private String imageUrl;
    private Double totalRating;
    private List<ReviewDto> reviews;
    private List<PostSimpleDto> posts;

}
