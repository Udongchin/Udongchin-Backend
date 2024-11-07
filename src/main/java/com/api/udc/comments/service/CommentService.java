package com.api.udc.comments.service;

import com.api.udc.comments.dto.CommentDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface CommentService{
   ResponseEntity<CustomApiResponse<?>> addComment(CommentDto commentDto);
   ResponseEntity<CustomApiResponse<?>> deleteComment(Long postId, Long commentId);
}
