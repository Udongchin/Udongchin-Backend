package com.api.udc.post.service;

import com.api.udc.post.dto.QADto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface QAService {
    CustomApiResponse<Long> createQA(String title, String content, MultipartFile image);
}
