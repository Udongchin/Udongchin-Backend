package com.api.udc.post.controller;

import com.api.udc.post.dto.FreeDetailResponseDto;
import com.api.udc.post.dto.UpdateFreeResponseDto;
import com.api.udc.post.dto.WarnDto;
import com.api.udc.post.service.FreeService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/post/community")
@RequiredArgsConstructor
public class FreeController {

    private final FreeService freeService;

    // 자유게시판 작성
    @PostMapping("/free")
    public CustomApiResponse<Long> createFree(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value= "mode", required = false ) String mode,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return freeService.createFree(title, content, mode, image);
    }

    // 자유게시판 개별조회
    @GetMapping("/free/{id}")
    public CustomApiResponse<FreeDetailResponseDto> getFreeDetail(@PathVariable Long id) {
        return freeService.getFreeDetail(id);
    }

    // 자유게시판 전체 조회 (실시간, 자유게시물 모두 조회)
    @GetMapping()
    public CustomApiResponse<List<Object>> getAllPosts() {
        return freeService.getAllPosts();
    }

    // 자유게시판 수정
    @PutMapping("/free/{id}")
    public CustomApiResponse<UpdateFreeResponseDto> updateFree(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "image", required = false) MultipartFile image)  {

        return freeService.updateFree(id, title, content, image);
    }

    // 자유게시판 삭제
    @DeleteMapping("/free/{id}")
    public CustomApiResponse<Void> deleteFree(@PathVariable Long id) {
        return freeService.deleteFree(id);
    }

    @PostMapping("/{postId}/warn")
    public ResponseEntity<CustomApiResponse<?>> warn(@PathVariable Long postId,@RequestBody WarnDto dto) {
        ResponseEntity<CustomApiResponse<?>> response=freeService.warn(postId,dto);
        return response;
    }

}