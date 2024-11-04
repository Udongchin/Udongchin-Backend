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
    public CustomApiResponse<Long> createQA(String title, String content, String mode, MultipartFile image) {
        // 제목과 내용이 비어있는지 확인
        if (title == null || title.trim().isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "제목을 작성해주세요.");
        }
        if (content == null || content.trim().isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "내용을 작성해주세요.");
        }
        // 모드가 유효한지 확인
        if (!mode.equals("realTimeRecord") && !mode.equals("realTimeQA")) {
            return CustomApiResponse.createFailWithoutData(400, "mode에 'realTimeRecord' 혹은 'realTimeQA'를 작성해주세요.");
        }
        try {
            String imageUrl = null;

            // 이미지가 있는 경우 파일 저장
            if (image != null && !image.isEmpty()) {
                imageUrl = saveImage(image);
            }

            // QA 엔티티 생성 및 저장
            QA qa = new QA(title, content, mode, imageUrl);
            qa = qaRepository.save(qa);

            // 성공 응답 반환
            return CustomApiResponse.createSuccess(200, qa.getId(), "QA가 성공적으로 작성되었습니다");

        } catch (Exception e) {
            return CustomApiResponse.createFailWithoutData(500, "QA가 작성되지 않았습니다.");
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
            throw new RuntimeException("이미지 저장 실패 " + originalFileName, e);
        }
        return uploadDir + fileName;
    }
}