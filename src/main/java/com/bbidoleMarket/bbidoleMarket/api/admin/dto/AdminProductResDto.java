package com.bbidoleMarket.bbidoleMarket.api.admin.dto;

import com.bbidoleMarket.bbidoleMarket.api.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductResDto {
    private Long id;
    private String title;
    private Integer price;
    private String name;
    private LocalDateTime createdAt;
    private Boolean isSold;

    public AdminProductResDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.price = post.getPrice();
        this.createdAt = post.getCreatedAt();
        this.isSold = post.getIsSold();
        this.name = post.getUser().getName();
    }
}
