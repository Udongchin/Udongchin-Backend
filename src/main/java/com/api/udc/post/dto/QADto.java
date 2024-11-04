package com.api.udc.post.dto;

import com.api.udc.domain.QA;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QADto {
    private String title;
    private String content;
    private String mode;
    private MultipartFile image;
}
