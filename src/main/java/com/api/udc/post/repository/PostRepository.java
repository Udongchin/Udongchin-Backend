package com.api.udc.post.repository;

import com.api.udc.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {



    List<Post> findByNickname(String nickname);
}
