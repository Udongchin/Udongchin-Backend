package com.api.udc.post.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QADetailResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String type;
    private String imageUrl;
    private int likesCount;
    private int commentCount;
    private boolean urgent;
    private String mode;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;
}
