package com.api.udc.post.service;

import com.api.udc.domain.Ad;
import com.api.udc.domain.Member;
import com.api.udc.domain.Post;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.post.dto.*;
import com.api.udc.post.repository.AdRepository;
import com.api.udc.util.Member.AuthenticationMemberUtils;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final MemberRepository memberRepository;
    private final AuthenticationMemberUtils memberUtils;
    private final AdRepository adRepository;
    private final String uploadDir = "images/";

    // Ad 작성
    @Override
    public CustomApiResponse<Long> createAd(String title, String content, String mode, MultipartFile image) {
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
        try {
            String imageUrl = null;

            // 이미지가 있는 경우 파일 저장
            if (image != null && !image.isEmpty()) {
                imageUrl = saveImage(image);
            }

            // Post 엔티티 생성 및 저장
            Post ad = new Post(title, content, mode, imageUrl,"홍보게시판",member.getNickname());
            ad = adRepository.save(ad);

            // 성공 응답 반환
            return CustomApiResponse.createSuccess(200, ad.getId(), "홍보게시판 게시물이 성공적으로 작성되었습니다");

        } catch (Exception e) {
            return CustomApiResponse.createFailWithoutData(500, "홍보게시판 게시물이 작성되지 않았습니다.");
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

    // Ad 개별 조회
    @Override
    public CustomApiResponse<AdDetailResponseDto> getAdDetail(Long id) {
        Optional<Post> optionalPost = adRepository.findById(id);

        // 게시물이 존재하지 않을 경우 실패 응답 반환
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        Post post = optionalPost.get();

        // 게시물 타입이 "홍보게시판"인지 확인
        if (!"홍보게시판".equals(post.getType())) {
            return CustomApiResponse.createFailWithoutData(400, "홍보게시판 글이 아닙니다.");
        }
        // 댓글 리스트 매핑
        List<CommentResponseDto> commentDtos = post.getComments().stream()
                .map(comment -> CommentResponseDto.builder()
                        .commenter(comment.getCommenter())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        // AdDetailResponseDto 빌드
        AdDetailResponseDto responseDto = AdDetailResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .contenter(String.valueOf(memberRepository.findByNickname(post.getNickname()).get().getMemberId()))
                .type(post.getType())
                .imageUrl(post.getImageUrl())
                .likesCount(post.getLikes())
                .commentCount(post.getComments().size())
                .createdAt(LocalDateTime.now())
                .comments(commentDtos)
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "홍보게시판 게시물이 정상적으로 개별 조회되었습니다.");
    }

    // 홍보게시판 전체 조회
    @Override
    public CustomApiResponse<List<Object>> getAllPosts() {
        List<AdDetailResponseDto> adPosts = adRepository.findAll().stream()
                .filter(free -> "홍보게시판".equals(free.getType()))
                .map(ad -> AdDetailResponseDto.builder()
                        .id(ad.getId())
                        .title(ad.getTitle())
                        .content(ad.getContent())
                        .contenter(String.valueOf(memberRepository.findByNickname(ad.getNickname()).get().getMemberId()))
                        .type(ad.getType())
                        .imageUrl(ad.getImageUrl())
                        .likesCount(ad.getLikes())
                        .commentCount(ad.getComments().size())
                        .createdAt(ad.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        List<Object> allPosts = new ArrayList<>(adPosts);

        return CustomApiResponse.createSuccess(200, allPosts, "홍보게시판 게시물이 정상적으로 전체 조회되었습니다.");
    }

    // 홍보 게시물 수정
    @Override
    public CustomApiResponse<UpdateAdResponseDto> updateAd(Long id, String title, String content, MultipartFile image) {
        Optional<Post> optionalPost = adRepository.findById(id);

        // 게시물이 존재하지 않을 경우 실패 응답 반환
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        Post ad = optionalPost.get();

        // 게시물 타입이 "홍보게시판"인지 확인
        if (!"홍보게시판".equals(ad.getType())) {
            return CustomApiResponse.createFailWithoutData(400, "홍보게시판 게시물이 아닙니다.");
        }

        String imageUrl = ad.getImageUrl();
        if (image != null && !image.isEmpty()) {
            imageUrl = saveImage(image);
        }

        // 엔티티 필드 업데이트
        ad.update(title, content, imageUrl);
        adRepository.save(ad);

        // UpdateAdResponseDto 빌드
        UpdateAdResponseDto responseDto = UpdateAdResponseDto.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .content(ad.getContent())
                .contenter(String.valueOf(memberRepository.findByNickname(ad.getNickname()).get().getMemberId()))
                .imageUrl(ad.getImageUrl())
                .updatedAt(LocalDateTime.now())
                .build();

        return CustomApiResponse.createSuccess(200, responseDto, "홍보게시판 게시물이 성공적으로 수정되었습니다.");
    }


    // 홍보 게시물 삭제
    @Override
    public CustomApiResponse<Void> deleteAd(Long id) {
        Optional<Post> optionalPost = adRepository.findById(id);

        // 게시물이 존재하지 않을 경우 실패 응답 반환
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        Post ad = optionalPost.get();

        // 게시물 타입이 "자유게시판"인지 확인
        if (!"홍보게시판".equals(ad.getType())) {
            return CustomApiResponse.createFailWithoutData(400, "홍보게시판 게시물이 아닙니다.");
        }
        adRepository.delete(ad);
        return CustomApiResponse.createSuccessWithoutData(200, "홍보게시판 게시물이 성공적으로 삭제되었습니다.");
    }
}