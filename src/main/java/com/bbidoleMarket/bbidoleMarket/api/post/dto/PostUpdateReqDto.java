package com.bbidoleMarket.bbidoleMarket.api.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostUpdateReqDto {

    @NotNull
    @Positive
    private long postId;

    @NotNull
    private String title;

    private String description;

    @Positive
    @NotNull
    private int price;

    @Positive
    @NotNull
    private long userId;
}
