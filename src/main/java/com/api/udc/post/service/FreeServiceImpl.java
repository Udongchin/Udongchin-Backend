package com.api.udc.post.service;

import com.api.udc.domain.Free;
import com.api.udc.post.dto.CommentResponseDto;
import com.api.udc.post.dto.FreeDetailResponseDto;
import com.api.udc.post.repository.FreeRepository;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreeServiceImpl implements FreeService {

    private final FreeRepository freeRepository;
    private final String uploadDir = "";

    // Free 작성
    @Override
    public CustomApiResponse<Long> createFree(String title, String content, MultipartFile image) {
        // 제목과 내용이 비어있는지 확인
        if (title == null || title.trim().isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "제목을 작성해주세요.");
        }
        if (content == null || content.trim().isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "내용을 작성해주세요.");
        }
        try {
            String imageUrl = null;

            // 이미지가 있는 경우 파일 저장
            if (image != null && !image.isEmpty()) {
                imageUrl = saveImage(image);
            }

            // Free 엔티티 생성 및 저장
            Free free = new Free(title, content, imageUrl);
            free = freeRepository.save(free);

            // 성공 응답 반환
            return CustomApiResponse.createSuccess(200, free.getId(), "자유게시판 게시물가 성공적으로 작성되었습니다");

        } catch (Exception e) {
            return CustomApiResponse.createFailWithoutData(500, "자유게시판 게시물가 작성되지 않았습니다.");
        }
    }

    // 이미지 저장
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

    // Free 개별 조회
    @Override
    public CustomApiResponse<FreeDetailResponseDto> getFreeDetail(Long id) {
        Free free = freeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("자유게시판 게시물 찾을 수 없음: " + id));

        // 댓글 리스트 매핑
        List<CommentResponseDto> commentDtos = free.getComments().stream()
                .map(comment -> CommentResponseDto.builder()
                        .commenter(comment.getCommenter())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // FreeDetailResponseDto 빌드
        FreeDetailResponseDto responseDto = FreeDetailResponseDto.builder()
                .id(free.getId())
                .title(free.getTitle())
                .content(free.getContent())
                .type("Free")
                .imageUrl(free.getImageUrl())
                .likesCount(free.getLikes())
                .commentCount(free.getComments().size())
                .createdAt(LocalDateTime.now())
                .comments(commentDtos)
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "자유게시판 게시물가 정상적으로 개별 조회되었습니다.");
    }
}