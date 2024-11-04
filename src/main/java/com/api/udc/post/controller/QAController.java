package com.api.udc.post.controller;

import com.api.udc.post.service.QAService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

}