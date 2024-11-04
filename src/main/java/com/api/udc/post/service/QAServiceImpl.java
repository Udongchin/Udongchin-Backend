package com.api.udc.post.service;

import com.api.udc.domain.QA;
import com.api.udc.post.dto.QARequestDto;
import com.api.udc.post.dto.QAResponseDto;
import com.api.udc.post.repository.QARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QAServiceImpl implements QAService {

    private final QARepository qaRepository;

    @Override
    public QAResponseDto createQA(QARequestDto qaRequestDto, MultipartFile image, String authToken) {
        // JWT 인증 로직 추가 (토큰 검증 및 사용자 권한 확인)

        if (qaRequestDto.getTitle() == null || qaRequestDto.getContent() == null) {
            throw new IllegalArgumentException("title, content가 비어있습니다.");
        }

        String imageUrl = uploadImage(image);  // 이미지 업로드 로직
        QA qa = QA.builder()
                .title(qaRequestDto.getTitle())
                .content(qaRequestDto.getContent())
                .imageUrl(imageUrl)
                .mode(qaRequestDto.getMode())
                .build();

        qaRepository.save(qa);

        return QAResponseDto.builder()
                .id(qa.getId())
                .title(qa.getTitle())
                .content(qa.getContent())
                .imageUrl(qa.getImageUrl())
                .mode(qa.getMode())
                .type(qa.getType())
                .createdAt(qa.getCreatedAt())
                .build();
    }

    private String uploadImage(MultipartFile image) {
        // 이미지 저장 로직 추가 (URL 리턴)
        return "https://사진경로/images/" + image.getOriginalFilename();
    }
}
