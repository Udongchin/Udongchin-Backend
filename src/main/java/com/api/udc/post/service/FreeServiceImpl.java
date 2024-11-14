package com.api.udc.post.service;

import com.api.udc.domain.Free;
import com.api.udc.domain.Member;
import com.api.udc.domain.Warn;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.post.dto.CommentResponseDto;
import com.api.udc.post.dto.FreeDetailResponseDto;
import com.api.udc.post.dto.UpdateFreeResponseDto;
import com.api.udc.post.dto.WarnDto;
import com.api.udc.post.repository.FreeRepository;
import com.api.udc.post.repository.PostRepository;
import com.api.udc.post.repository.QARepository;
import com.api.udc.post.repository.WarnRepository;
import com.api.udc.util.Member.AuthenticationMemberUtils;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.api.udc.domain.Post;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FreeServiceImpl implements FreeService {

    private final FreeRepository freeRepository;
    private final QARepository qaRepository;
    private final WarnRepository warnRepository;
    private final AuthenticationMemberUtils memberUtils;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final String uploadDir = "";

    // Free 작성
    @Override
    public CustomApiResponse<Long> createFree(String title, String content, String mode, MultipartFile image) {
        // 제목과 내용이 비어있는지 확인
        String currentMemberId = memberUtils.getCurrentMemberId();
        Optional<Member> optionalMember=memberRepository.findByMemberId(currentMemberId);
        Member member=optionalMember.get();
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
            Post free = new Post(title, content, mode, imageUrl,"자유게시판",member.getNickname());
            free = freeRepository.save(free);

            // 성공 응답 반환
            return CustomApiResponse.createSuccess(200, free.getId(), "자유게시판 게시물이 성공적으로 작성되었습니다");

        } catch (Exception e) {
            return CustomApiResponse.createFailWithoutData(500, "자유게시판 게시물이 작성되지 않았습니다.");
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
        Optional<Post> optionalPost = freeRepository.findById(id);

        // 게시물이 존재하지 않을 경우 실패 응답 반환
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        Post post = optionalPost.get();

        // 게시물 타입이 "자유게시판" 또는 "실시간"인지 확인
        if (!"자유게시판".equals(post.getType()) && !"실시간".equals(post.getType())) {
            return CustomApiResponse.createFailWithoutData(400, "자유게시판 혹은 실시간 글이 아닙니다.");
        }

        // 댓글 리스트 매핑
        List<CommentResponseDto> commentDtos = post.getComments().stream()
                .map(comment -> CommentResponseDto.builder()
                        .commenter(comment.getCommenter())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // FreeDetailResponseDto 빌드
        FreeDetailResponseDto responseDto = FreeDetailResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .type(post.getType())
                .imageUrl(post.getImageUrl())
                .likesCount(post.getLikes())
                .commentCount(post.getComments().size())
                .createdAt(post.getCreatedAt())
                .comments(commentDtos)
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "게시물이 정상적으로 조회되었습니다.");
    }


    // 자유게시판 전체 조회
    @Override
    public CustomApiResponse<List<Object>> getAllPosts() {
        // "자유게시판" 타입인 게시물만 조회


        List<FreeDetailResponseDto> freePosts = freeRepository.findAll().stream()
                .filter(free -> "자유게시판".equals(free.getType()))
                .map(free -> FreeDetailResponseDto.builder()
                        .id(free.getId())
                        .title(free.getTitle())
                        .content(free.getContent())
                        .contenter(String.valueOf(memberRepository.findByNickname(free.getNickname()).get().getMemberId()))
                        .type(free.getType())
                        .imageUrl(free.getImageUrl())
                        .likesCount(free.getLikes())
                        .commentCount(free.getComments().size())
                        .createdAt(free.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // "실시간" 타입인 게시물만 조회
        List<FreeDetailResponseDto> qaPosts = qaRepository.findAll().stream()
                .filter(qa -> "실시간".equals(qa.getType()))
                .map(qa -> FreeDetailResponseDto.builder()
                        .id(qa.getId())
                        .title(qa.getTitle())
                        .content(qa.getContent())
                        .contenter(String.valueOf(memberRepository.findByNickname(qa.getNickname()).get().getMemberId()))
                        .type(qa.getType())
                        .imageUrl(qa.getImageUrl())
                        .likesCount(qa.getLikes())
                        .commentCount(qa.getComments().size())
                        .createdAt(qa.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // 자유게시판과 실시간 게시물들을 시간순으로 정렬하여 병합
        List<Object> allPosts = Stream.concat(freePosts.stream(), qaPosts.stream())
                .sorted(Comparator.comparing(FreeDetailResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return CustomApiResponse.createSuccess(200, allPosts, "자유게시판 게시물이 정상적으로 전체 조회되었습니다.");
    }


    // 자유 게시물 수정
    @Override
    public CustomApiResponse<UpdateFreeResponseDto> updateFree(Long id, String title, String content, MultipartFile image) {
        Optional<Post> optionalPost = freeRepository.findById(id);

        // 게시물이 존재하지 않을 경우 실패 응답 반환
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        Post free = optionalPost.get();

        // 게시물 타입이 "자유게시판"인지 확인
        if (!"자유게시판".equals(free.getType())) {
            return CustomApiResponse.createFailWithoutData(400, "자유게시판 게시물이 아닙니다.");
        }

        String imageUrl = free.getImageUrl();
        if (image != null && !image.isEmpty()) {
            imageUrl = saveImage(image);
        }

        // 엔티티 필드 업데이트
        free.update(title, content, imageUrl);
        freeRepository.save(free);

        // UpdateFreeResponseDto 빌드
        UpdateFreeResponseDto responseDto = UpdateFreeResponseDto.builder()
                .id(free.getId())
                .title(free.getTitle())
                .content(free.getContent())
                .imageUrl(free.getImageUrl())
                .updatedAt(LocalDateTime.now())
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "자유게시판 게시물이 성공적으로 수정되었습니다.");
    }

    // 자유 게시물 삭제
    @Override
    public CustomApiResponse<Void> deleteFree(Long id) {
        Optional<Post> optionalPost = freeRepository.findById(id);

        // 게시물이 존재하지 않을 경우 실패 응답 반환
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        Post free = optionalPost.get();

        // 게시물 타입이 "자유게시판"인지 확인
        if (!"자유게시판".equals(free.getType())) {
            return CustomApiResponse.createFailWithoutData(400, "자유게시판 게시물이 아닙니다.");
        }

        freeRepository.delete(free);
        return CustomApiResponse.createSuccessWithoutData(200, "자유게시판 게시물이 성공적으로 삭제되었습니다.");
    }

    @Override
    public ResponseEntity<CustomApiResponse<?>> warn(Long id, WarnDto dto) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(
                    HttpStatus.BAD_REQUEST.value(),
                    "해당 게시글이 존재하지 않습니다."
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Warn warn=new Warn(id,dto.getReason(), dto.getCustomReason());
        warnRepository.save(warn);
        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, warn, "신고가 정상적으로 접수되었습니다"));
    }
}