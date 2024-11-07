package com.api.udc.like.controller;

import com.api.udc.like.service.LikeService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요
    @PostMapping("/{id}")
    public CustomApiResponse<Void> likePost(@PathVariable Long id) {
        return likeService.likePost(id);
    }

    // 좋아요 취소
    @DeleteMapping("/{id}")
    public CustomApiResponse<Void> unlikePost(@PathVariable Long id) {
        return likeService.unlikePost(id);
    }

    // 좋아요 상태 확인
    @GetMapping("/{id}")
    public CustomApiResponse<Boolean> isPostLiked(@PathVariable Long id) {
        return likeService.isPostLiked(id);
    }
}
