package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDetailResDto {

    private long userId;
    private String nickname;
    private String imageUrl;
    private Double totalRating;
    private Boolean isActive;

}
