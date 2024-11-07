package com.api.udc.like.service;

import com.api.udc.domain.Member;
import com.api.udc.domain.Post;
import com.api.udc.like.domain.Like;
import com.api.udc.like.repository.LikeRepository;
import com.api.udc.post.repository.PostRepository;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.util.Member.AuthenticationMemberUtils;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AuthenticationMemberUtils memberUtils;

    @Override
    public CustomApiResponse<Void> likePost(Long id) {
        // 게시물과 멤버를 확인
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        String currentMemberId = memberUtils.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findByMemberId(currentMemberId);
        if (optionalMember.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "회원 정보를 찾을 수 없습니다.");
        }

        Post post = optionalPost.get();
        Member member = optionalMember.get();

        // 이미 좋아요가 되어 있는지 확인
        if (likeRepository.findByPostAndMember(post, member).isPresent()) {
            return CustomApiResponse.createFailWithoutData(400, "이미 좋아요를 누르셨습니다.");
        }

        // 좋아요 저장
        Like like = new Like(post, member);
        likeRepository.save(like);
        return CustomApiResponse.createSuccessWithoutData(200, "좋아요가 추가되었습니다.");
    }

    @Override
    public CustomApiResponse<Void> unlikePost(Long id) {
        // 게시물과 멤버를 확인
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        String currentMemberId = memberUtils.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findByMemberId(currentMemberId);
        if (optionalMember.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "회원 정보를 찾을 수 없습니다.");
        }

        Post post = optionalPost.get();
        Member member = optionalMember.get();

        // 좋아요 상태 확인 후 삭제
        Optional<Like> optionalLike = likeRepository.findByPostAndMember(post, member);
        if (optionalLike.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "좋아요가 설정되어 있지 않습니다.");
        }

        likeRepository.delete(optionalLike.get());
        return CustomApiResponse.createSuccessWithoutData(200, "좋아요가 취소되었습니다.");
    }

    @Override
    public CustomApiResponse<Boolean> isPostLiked(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(404, "게시물을 찾을 수 없습니다.");
        }

        String currentMemberId = memberUtils.getCurrentMemberId();
        Optional<Member> optionalMember = memberRepository.findByMemberId(currentMemberId);
        if (optionalMember.isEmpty()) {
            return CustomApiResponse.createFailWithoutData(400, "회원 정보를 찾을 수 없습니다.");
        }

        Post post = optionalPost.get();
        Member member = optionalMember.get();

        // 좋아요 상태 확인
        boolean isLiked = likeRepository.findByPostAndMember(post, member).isPresent();
        return CustomApiResponse.createSuccess(200, isLiked, "좋아요 상태 조회 성공");
    }
}
