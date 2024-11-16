package com.api.udc.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class LikeDetailDto {
    private int likesCount;
    private String liker;
    private String title;
    private Long post_id;
    private String imageUrl;
}
