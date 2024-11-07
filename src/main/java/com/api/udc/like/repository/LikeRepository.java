package com.api.udc.like.repository;

import com.api.udc.like.domain.Like;
import com.api.udc.domain.Post;
import com.api.udc.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndMember(Post post, Member member);
    long countByPost(Post post);
}
