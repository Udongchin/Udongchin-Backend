package com.api.udc.post.controller;

import com.api.udc.post.dto.FreeDetailResponseDto;
import com.api.udc.post.service.FreeService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/post/community/free")
@RequiredArgsConstructor
public class FreeController {

    private final FreeService freeService;

    @PostMapping
    public CustomApiResponse<Long> createFree(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return freeService.createFree(title, content, image);
    }

    //  개별조회
    @GetMapping("/{id}")
    public CustomApiResponse<FreeDetailResponseDto> getFreeDetail(@PathVariable Long id) {
        return freeService.getFreeDetail(id);
    }

}