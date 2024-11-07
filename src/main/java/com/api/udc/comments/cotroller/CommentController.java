package com.api.udc.comments.cotroller;

import com.api.udc.comments.dto.CommentDto;
import com.api.udc.comments.service.CommentService;

import com.api.udc.domain.Member;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.util.Member.AuthenticationMemberUtils;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/")
public class CommentController {
    private final CommentService commentService;
    private final MemberRepository memberRepository;
    private final AuthenticationMemberUtils memberUtils;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CustomApiResponse<?>> postComment(@PathVariable Long postId, @RequestBody CommentDto dto) {
        String currentMemberId = memberUtils.getCurrentMemberId();
        Optional<Member> optionalMember=memberRepository.findByMemberId(currentMemberId);
        Member member=optionalMember.get();
        dto.setCommenter(member.getNickname());
        dto.setId(postId);
        ResponseEntity<CustomApiResponse<?>> result = commentService.addComment(dto);
        return result;
    }
    //ㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗ
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CustomApiResponse<?>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return commentService.deleteComment(postId, commentId);
    }
}
