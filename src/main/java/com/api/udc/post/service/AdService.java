package com.api.udc.post.service;

import com.api.udc.post.dto.AdDetailResponseDto;
import com.api.udc.post.dto.UpdateAdResponseDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdService {
    CustomApiResponse<Long> createAd(String title, String content, String mode, MultipartFile image);
    CustomApiResponse<AdDetailResponseDto> getAdDetail(Long id);
    CustomApiResponse<List<Object>> getAllPosts();
    CustomApiResponse<UpdateAdResponseDto> updateAd(Long id, String title, String content, MultipartFile image);
    CustomApiResponse<Void> deleteAd(Long id);
}
