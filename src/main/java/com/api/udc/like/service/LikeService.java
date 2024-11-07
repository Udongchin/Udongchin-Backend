package com.api.udc.like.service;

import com.api.udc.util.response.CustomApiResponse;

public interface LikeService {
    CustomApiResponse<Void> likePost(Long id);
    CustomApiResponse<Void> unlikePost(Long id);
    CustomApiResponse<Boolean> isPostLiked(Long id);
}
