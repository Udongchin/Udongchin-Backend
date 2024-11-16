package com.api.udc.post.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String contenter;
    private String type;
    private String imageUrl;
    private int likesCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;
}
