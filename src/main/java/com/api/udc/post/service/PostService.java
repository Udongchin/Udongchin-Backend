package com.api.udc.post.service;

import com.api.udc.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<CustomApiResponse<?>> getMyPost(String memberId);

    ResponseEntity<CustomApiResponse<?>> getMyComment(String currentMemberId);

    ResponseEntity<CustomApiResponse<?>> getMyLike(String currentMemberId);
}
