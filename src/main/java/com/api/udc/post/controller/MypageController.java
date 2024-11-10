package com.api.udc.post.controller;

import com.api.udc.domain.Member;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.post.repository.PostRepository;
import com.api.udc.post.service.PostService;
import com.api.udc.util.Member.AuthenticationMemberUtils;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final PostService postService;
    private final AuthenticationMemberUtils memberUtils;
    private final MemberRepository memberRepository;

    @GetMapping("/post")
    public ResponseEntity<CustomApiResponse<?>> getMyPost(String id) {
        String currentMemberId = memberUtils.getCurrentMemberId();
        ResponseEntity<CustomApiResponse<?>> response=postService.getMyPost(currentMemberId);
        return response;

    }
    @GetMapping("/comment")
    public ResponseEntity<CustomApiResponse<?>> getMyComment(String id) {
        String currentMemberId = memberUtils.getCurrentMemberId();
        ResponseEntity<CustomApiResponse<?>> response=postService.getMyComment(currentMemberId);
        return response;
    }
    @GetMapping("/like")

}
