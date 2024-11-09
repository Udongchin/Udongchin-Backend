package com.api.udc.post.service;

import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QAService {
    CustomApiResponse<Long> createQA(String title, String content, String mode, MultipartFile image, List<String> location);
    CustomApiResponse<QADetailResponseDto> getQADetail(Long id);
    ResponseEntity<CustomApiResponse<?>> urgent(Long id);
    CustomApiResponse<List<QADetailResponseDto>> getAllQA();
}
