package com.api.udc.post.controller;

import com.api.udc.post.dto.CommunityDetailResponseDto;
import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.post.service.CommunityService;
import com.api.udc.post.service.QAService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/post/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    public CustomApiResponse<Long> createCommunity(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return communityService.createCommunity(title, content, image);
    }

    // QA 개별조회
    @GetMapping("/{id}")
    public CustomApiResponse<CommunityDetailResponseDto> getCommunityDetail(@PathVariable Long id) {
        return communityService.getCommunityDetail(id);
    }

}