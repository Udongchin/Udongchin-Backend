package com.api.udc.post.repository;

import com.api.udc.domain.Post;
import com.api.udc.domain.QA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QARepository extends JpaRepository<Post, Long> {
    List<Post> findByNickname(String nickname);

    List<Post> findAllByTypeAndCreatedAtBefore(String type, LocalDateTime dateTime);
}