package com.api.udc.post.repository;

import com.api.udc.domain.Ad;
import com.api.udc.domain.Free;
import com.api.udc.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Post, Long> {
}