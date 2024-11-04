package com.api.udc.post.service;

import com.api.udc.post.dto.QARequestDto;
import com.api.udc.post.dto.QAResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface QAService {
    QAResponseDto createQA(QARequestDto qaRequestDto, MultipartFile image, String authToken);
}
