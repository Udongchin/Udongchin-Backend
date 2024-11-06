package com.api.udc.post.service;

import com.api.udc.post.dto.FreeDetailResponseDto;
import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FreeService {
    CustomApiResponse<Long> createFree(String title, String content, MultipartFile image);
    CustomApiResponse<FreeDetailResponseDto> getFreeDetail(Long id);
}
