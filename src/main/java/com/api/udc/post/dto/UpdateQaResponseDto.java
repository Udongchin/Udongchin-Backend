package com.api.udc.post.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
@Data
@Getter
public class UpdateQaResponseDto {
    private Long id;
    private String title;
    private String content;
    private String type;
    private String imageUrl;
    private int likesCount;
    private int commentCount;
    private boolean urgent;
    private LocalDateTime updatedAt;

    // Constructor
    public UpdateQaResponseDto(Long id, String title, String content, String type, String imageUrl, int likesCount, int commentCount, boolean urgent, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.imageUrl = imageUrl;
        this.likesCount = likesCount;
        this.commentCount = commentCount;
        this.urgent = urgent;
        this.updatedAt = updatedAt;
    }



}
