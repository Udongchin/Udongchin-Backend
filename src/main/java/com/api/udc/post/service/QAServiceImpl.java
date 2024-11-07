package com.api.udc.post.service;

import com.api.udc.domain.Member;
import com.api.udc.domain.QA;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.post.dto.CommentResponseDto;
import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.post.repository.QARepository;
import com.api.udc.util.Member.AuthenticationMemberUtils;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.api.udc.domain.Post;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QAServiceImpl implements QAService {
    private final MemberRepository memberRepository;
    private final AuthenticationMemberUtils memberUtils;
    private final QARepository qaRepository;
    private final String uploadDir = "";

    // QA 작성
    @Override
    public CustomApiResponse<Long> createQA(String title, String content, String mode, MultipartFile image) {
        String currentMemberId = memberUtils.getCurrentMemberId();
        Optional<Member> optionalMember=memberRepository.findByMemberId(currentMemberId);
        Member member=optionalMember.get();
        // 제목과 내용이 비어있는지 확인
        if (title == null || title.trim().isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "제목을 작성해주세요.");
        }
        if (content == null || content.trim().isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "내용을 작성해주세요.");
        }
        // 모드가 유효한지 확인
        if (!mode.equals("실시간 기록") && !mode.equals("실시간 Q&A")) {
            return CustomApiResponse.createFailWithoutData(400, "mode에 '실시간 기록' 혹은 '실시간 Q&A'를 작성해주세요.");
        }
        try {
            String imageUrl = null;

            // 이미지가 있는 경우 파일 저장
            if (image != null && !image.isEmpty()) {
                imageUrl = saveImage(image);
            }

            // QA 엔티티 생성 및 저장
            Post qa = new Post(title, content, mode, imageUrl,"실시간",member.getNickname());
            qa = qaRepository.save(qa);

            // 성공 응답 반환
            return CustomApiResponse.createSuccess(200, qa.getId(), "실시간 게시물이 성공적으로 작성되었습니다");

        } catch (Exception e) {
            return CustomApiResponse.createFailWithoutData(500, "실시간 게시물이 작성되지 않았습니다.");
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

    // QA 개별 조회
    @Override
    public CustomApiResponse<QADetailResponseDto> getQADetail(Long id) {
        Post qa = qaRepository.findById(id)
                .orElse(null);

        // If the post is not found, return a failure response
        if (qa == null) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        // Check if the type is "실시간"
        if (!"실시간".equals(qa.getType())) {
            return CustomApiResponse.createFailWithoutData(400, "실시간 게시물이 아닙니다.");
        }

        // 댓글 리스트 매핑
        List<CommentResponseDto> commentDtos = qa.getComments().stream()
                .map(comment -> CommentResponseDto.builder()
                        .commenter(comment.getCommenter())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // QADetailResponseDto 빌드
        QADetailResponseDto responseDto = QADetailResponseDto.builder()
                .id(qa.getId())
                .title(qa.getTitle())
                .nickname(qa.getNickname())
                .content(qa.getContent())
                .type(qa.getType())
                .imageUrl(qa.getImageUrl())
                .likesCount(qa.getLikes())
                .commentCount(qa.getComments().size())
                .urgent(qa.isUrgent())
                .mode(qa.getMode())
                .createdAt(qa.getCreatedAt())
                .comments(commentDtos)
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "실시간 글이 정상적으로 개별 조회되었습니다.");
    }


}