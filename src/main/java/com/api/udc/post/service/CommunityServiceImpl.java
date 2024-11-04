package com.api.udc.post.service;

import com.api.udc.domain.Community;
import com.api.udc.post.dto.CommentResponseDto;
import com.api.udc.post.dto.CommunityDetailResponseDto;
import com.api.udc.post.repository.CommunityRepository;
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
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final String uploadDir = "";

    // Community 작성
    @Override
    public CustomApiResponse<Long> createCommunity(String title, String content, MultipartFile image) {
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

            // Community 엔티티 생성 및 저장
            Community community = new Community(title, content, imageUrl);
            community = communityRepository.save(community);

            // 성공 응답 반환
            return CustomApiResponse.createSuccess(200, community.getId(), "community가 성공적으로 작성되었습니다");

        } catch (Exception e) {
            return CustomApiResponse.createFailWithoutData(500, "community가 작성되지 않았습니다.");
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

    // Community 개별 조회
    @Override
    public CustomApiResponse<CommunityDetailResponseDto> getCommunityDetail(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community 찾을 수 없음: " + id));

        // 댓글 리스트 매핑
        List<CommentResponseDto> commentDtos = community.getComments().stream()
                .map(comment -> CommentResponseDto.builder()
                        .commenter(comment.getCommenter())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // CommunityDetailResponseDto 빌드
        CommunityDetailResponseDto responseDto = CommunityDetailResponseDto.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .type("Community")
                .imageUrl(community.getImageUrl())
                .likesCount(community.getLikes())
                .commentCount(community.getComments().size())
                .createdAt(LocalDateTime.now())
                .comments(commentDtos)
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "Community가 정상적으로 개별 조회되었습니다.");
    }
}