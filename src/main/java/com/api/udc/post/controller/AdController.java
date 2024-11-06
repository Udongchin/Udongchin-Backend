package com.api.udc.post.controller;

import com.api.udc.post.dto.AdDetailResponseDto;
import com.api.udc.post.dto.FreeDetailResponseDto;
import com.api.udc.post.service.AdService;
import com.api.udc.post.service.FreeService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/post/community/ad")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @PostMapping
    public CustomApiResponse<Long> createAd(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return adService.createAd(title, content, image);
    }

    //  개별조회
    @GetMapping("/{id}")
    public CustomApiResponse<AdDetailResponseDto> getAdDetail(@PathVariable Long id) {
        return adService.getAdDetail(id);
    }

}