package com.api.udc.post.repository;

import com.api.udc.domain.Ad;
import com.api.udc.domain.Free;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
}