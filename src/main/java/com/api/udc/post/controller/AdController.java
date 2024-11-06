package com.api.udc.post.controller;

import com.api.udc.post.dto.AdDetailResponseDto;
import com.api.udc.post.dto.AdDetailResponseDto;
import com.api.udc.post.dto.UpdateAdResponseDto;
import com.api.udc.post.service.AdService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/post/community/ad")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    // 홍보게시물 작성
    @PostMapping
    public CustomApiResponse<Long> createAd(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return adService.createAd(title, content, image);
    }

    // 개별조회
    @GetMapping("/{id}")
    public CustomApiResponse<AdDetailResponseDto> getAdDetail(@PathVariable Long id) {
        return adService.getAdDetail(id);
    }

    // 홍보게시판 전체 조회
    @GetMapping
    public CustomApiResponse<List<Object>> getAllPosts() {
        return adService.getAllPosts();
    }

    // 홍보게시판 수정
    @PutMapping("/{id}")
    public CustomApiResponse<UpdateAdResponseDto> updateAd(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return adService.updateAd(id, title, content, image);
    }

    // 자유게시판 삭제
    @DeleteMapping("/{id}")
    public CustomApiResponse<Void> deleteAd(@PathVariable Long id) {
        return adService.deleteAd(id);
    }

}