package com.api.udc.post.service;

import com.api.udc.domain.Member;
import com.api.udc.domain.QA;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.post.dto.CommentResponseDto;
import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.post.dto.UpdateQaResponseDto;
import com.api.udc.post.repository.QARepository;
import com.api.udc.util.Member.AuthenticationMemberUtils;
import com.api.udc.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.api.udc.domain.Post;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QAServiceImpl implements QAService {
    private final MemberRepository memberRepository;
    private final AuthenticationMemberUtils memberUtils;
    private final QARepository qaRepository;
    private final String uploadDir = "images/";

    // QA 작성
    @Override
    public CustomApiResponse<Long> createQA(String title, String content, String mode, MultipartFile image,List<String> location) {
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
            qa.setLocation(location);
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
                .location(qa.getLocation())
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "실시간 글이 정상적으로 개별 조회되었습니다.");
    }

    @Override
    public CustomApiResponse<List<QADetailResponseDto>> getAllQA() {

        List<QADetailResponseDto> qaDetails = new ArrayList<>();
        List<Post> qaPosts = qaRepository.findAll();

        for (Post qa : qaPosts) {
            if (!"실시간".equals(qa.getType())) {
                continue;
            }
            List<CommentResponseDto> commentDtos = qa.getComments().stream()
                    .map(comment -> CommentResponseDto.builder()
                            .commenter(comment.getCommenter())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());

            QADetailResponseDto qaDetailResponseDto = QADetailResponseDto.builder()
                    .id(qa.getId())
                    .nickname(qa.getNickname())
                    .title(qa.getTitle())
                    .content(qa.getContent())
                    .type(qa.getType())
                    .imageUrl(qa.getImageUrl())
                    .likesCount(qa.getLikes())
                    .commentCount(qa.getCommentCount())
                    .comments(commentDtos)
                    .urgent(qa.isUrgent())
                    .mode(qa.getMode())
                    .createdAt(qa.getCreatedAt())
                    .location(qa.getLocation())
                    .build();

            qaDetails.add(qaDetailResponseDto);
        }
        return CustomApiResponse.createSuccess(200, qaDetails, "실시간 글이 정상적으로 조회되었습니다.");
    }

    // 긴급
    @Override
    public ResponseEntity<CustomApiResponse<?>> urgent(Long id) {
        Optional<Post> optionalPost = qaRepository.findById(id);

        if (optionalPost.isEmpty()) {
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(
                    HttpStatus.BAD_REQUEST.value(),
                    "해당 ID의 게시글이 존재하지 않습니다."
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Post qa = optionalPost.get();
        if (!"실시간".equals(qa.getType())) {
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(
                    HttpStatus.BAD_REQUEST.value(),
                    "해당 게시글이 존재하지 않습니다."
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        qa.setUrgent(true);
        qaRepository.save(qa);
        UpdateQaResponseDto qa2=new UpdateQaResponseDto(
                qa.getId(),
                qa.getTitle(),
                qa.getContent(),
                qa.getType(),
                qa.getImageUrl(),
                qa.getLikes(),
                qa.getComments().size(),
                qa.isUrgent(),
                qa.getUpdatedAt()

        );
        // Save the updated post

        CustomApiResponse<?> response = CustomApiResponse.createSuccess(200,qa2,"긴급 표시가 설정되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 7일 지난 게시글 삭제 스케줄링
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    @Transactional
    public void deleteOldPosts() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        // 7일이 지난 "실시간" 게시글 삭제
        List<Post> postsToDelete = qaRepository.findAllByTypeAndCreatedAtBefore("실시간", sevenDaysAgo);

        if (!postsToDelete.isEmpty()) {
            qaRepository.deleteAll(postsToDelete);
            System.out.println("7일 지난 게시글 삭제 완료: " + postsToDelete.size() + "개 삭제됨");
        } else {
            System.out.println("삭제할 게시글 없음");
        }
    }

    // 긴급 건을 24시간 이후 false로 설정하는 스케줄링
    @Scheduled(cron = "0 0 * * * ?") // 매 정시에 실행
    @Transactional
    public void resetUrgentStatus() {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);

        // 24시간이 지난 "긴급" 상태의 게시글 조회
        List<Post> urgentPosts = qaRepository.findAllByTypeAndUrgentAndUpdatedAtBefore("실시간", true, twentyFourHoursAgo);

        if (!urgentPosts.isEmpty()) {
            urgentPosts.forEach(post -> post.setUrgent(false));
            qaRepository.saveAll(urgentPosts);
            System.out.println("긴급 상태 초기화 완료: " + urgentPosts.size() + "개 게시글의 긴급 상태가 해제됨");
        } else {
            System.out.println("긴급 상태를 초기화할 게시글이 없음");
        }
    }
}