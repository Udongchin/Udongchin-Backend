package com.api.udc.post.service;

import com.api.udc.post.dto.FreeDetailResponseDto;
import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FreeService {
    CustomApiResponse<Long> createFree(String title, String content, MultipartFile image);
    CustomApiResponse<FreeDetailResponseDto> getFreeDetail(Long id);
    CustomApiResponse<List<Object>> getAllPosts();
    CustomApiResponse<Long> updateFree(Long id, String title, String content, MultipartFile image);
    CustomApiResponse<Void> deleteFree(Long id);
}
