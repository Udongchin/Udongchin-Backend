package com.api.udc.post.service;

import com.api.udc.post.dto.AdDetailResponseDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AdService {
    CustomApiResponse<Long> createAd(String title, String content, MultipartFile image);
    CustomApiResponse<AdDetailResponseDto> getAdDetail(Long id);
}
