package com.api.udc.post.service;

import com.api.udc.post.dto.CommunityDetailResponseDto;
import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CommunityService {
    CustomApiResponse<Long> createCommunity(String title, String content, MultipartFile image);
    CustomApiResponse<CommunityDetailResponseDto> getCommunityDetail(Long id);
}
