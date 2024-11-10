package com.api.udc.post.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class CommentDetailDto {
    private String commenter;
    private String content;
    private LocalDateTime createdAt;
    private Long postId;
    private String postTitle;
}
