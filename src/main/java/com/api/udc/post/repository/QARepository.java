package com.api.udc.post.repository;

import com.api.udc.domain.Post;
import com.api.udc.domain.QA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QARepository extends JpaRepository<Post, Long> {
}