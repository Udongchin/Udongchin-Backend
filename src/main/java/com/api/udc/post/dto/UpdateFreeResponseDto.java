package com.api.udc.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateFreeResponseDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime updatedAt;
}