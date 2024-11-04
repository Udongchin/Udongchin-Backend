package com.api.udc.post.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QARequestDto {
    private String title;
    private String content;
    private String mode;
    private String imageUrl;
}
