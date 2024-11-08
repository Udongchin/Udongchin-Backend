package com.api.udc.post.controller;

import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.post.service.QAService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("api/post/qa")
@RequiredArgsConstructor
public class QAController {

    private final QAService qaService;

    @PostMapping
    public CustomApiResponse<Long> createQA(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("mode") String mode,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return qaService.createQA(title, content, mode, image);
    }

    // QA 개별조회
    @GetMapping("/{id}")
    public CustomApiResponse<QADetailResponseDto> getQADetail(@PathVariable Long id) {
        return qaService.getQADetail(id);
    }

    @PostMapping("/{postId}/urgent")
    public ResponseEntity<CustomApiResponse<?>> urgent(@PathVariable Long postId) {
        ResponseEntity<CustomApiResponse<?>> response = qaService.urgent(postId);
        return response;
    }
}