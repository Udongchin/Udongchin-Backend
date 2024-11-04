package com.api.udc.post.service;

import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface QAService {
    CustomApiResponse<Long> createQA(String title, String content, String mode, MultipartFile image);
    CustomApiResponse<QADetailResponseDto> getQADetail(Long id);
}
