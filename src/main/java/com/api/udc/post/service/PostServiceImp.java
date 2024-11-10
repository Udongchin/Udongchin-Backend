package com.api.udc.post.service;

import com.api.udc.comments.dto.CommentDto;
import com.api.udc.comments.repository.CommentRepository;
import com.api.udc.domain.Comment;
import com.api.udc.domain.Member;
import com.api.udc.domain.Post;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.post.dto.CommentDetailDto;
import com.api.udc.post.dto.CommentResponseDto;
import com.api.udc.post.dto.PostDeatilResponseDto;
import com.api.udc.post.dto.QADetailResponseDto;
import com.api.udc.post.repository.PostRepository;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;


    public ResponseEntity<CustomApiResponse<?>> getMyPost(String id) {

        Optional<Member> optionalMember=memberRepository.findByMemberId(id);
        Member member=optionalMember.get();
        List<PostDeatilResponseDto> qaDetails = new ArrayList<>();
        List<Post> qaPosts = postRepository.findByNickname(member.getNickname());

        for (Post qa : qaPosts) {

            List<CommentResponseDto> commentDtos = qa.getComments().stream()
                    .map(comment -> CommentResponseDto.builder()
                            .commenter(comment.getCommenter())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());
            PostDeatilResponseDto postDetailResponseDto = PostDeatilResponseDto.builder()
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


            qaDetails.add(postDetailResponseDto);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(200,qaDetails,"게시물 조회가 완료되었습니다."));
    }

    public ResponseEntity<CustomApiResponse<?>> getMyComment(String id) {
        Optional<Member> optionalMember=memberRepository.findByMemberId(id);
        Member member=optionalMember.get();
        List<CommentDetailDto> commentDetails = new ArrayList<>();
        List<Comment> comments=commentRepository.findByCommenter(member.getNickname());
        for (Comment comment : comments) {
            CommentDetailDto commentResponseDto= CommentDetailDto.builder()
                    .commenter(comment.getCommenter())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .postId(comment.getPost().getId())
                    .postTitle(comment.getPost().getTitle())
                    .build();
            commentDetails.add(commentResponseDto);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomApiResponse.createSuccess(200,commentDetails,"게시물 조회가 완료되었습니다."));
    }
}
