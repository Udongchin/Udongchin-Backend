package com.api.udc.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QAResponseDto {
    private Long id;
    private String title;
    private String content;
    private String type;
    private String imageUrl;
    private String mode;
    private LocalDateTime createdAt;
}
