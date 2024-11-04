package com.api.udc.post.service;

import com.api.udc.domain.QA;
import com.api.udc.post.repository.QARepository;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QAServiceImpl implements QAService {

    private final QARepository qaRepository;
    private final String uploadDir = "";

    @Override
    public CustomApiResponse<Long> createQA(String title, String content, MultipartFile image) {
        try {
            String imageUrl = null;

            // 이미지가 있는 경우 파일 저장
            if (image != null && !image.isEmpty()) {
                imageUrl = saveImage(image);
            }

            // QA 엔티티 생성 및 저장
            QA qa = new QA(title, content, false, imageUrl);
            qa = qaRepository.save(qa);

            // 성공 응답 반환
            return CustomApiResponse.createSuccess(201, qa.getId(), "QA created successfully");

        } catch (Exception e) {
            return CustomApiResponse.createFailWithoutData(500, "Failed to create QA post");
        }
    }

    private String saveImage(MultipartFile image) {
        String originalFileName = StringUtils.cleanPath(image.getOriginalFilename());
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(image.getInputStream(), uploadPath.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image file " + originalFileName, e);
        }
        return uploadDir + fileName;
    }
}