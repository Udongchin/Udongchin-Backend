package com.api.udc.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateAdResponseDto {
    private Long id;
    private String title;
    private String content;
    private String contenter;
    private String imageUrl;
    private LocalDateTime updatedAt;
}
