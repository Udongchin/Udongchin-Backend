package com.api.udc.comments.service;

import com.api.udc.comments.dto.CommentDto;
import com.api.udc.comments.repository.CommentRepository;
import com.api.udc.domain.Comment;
import com.api.udc.domain.Post;
import com.api.udc.post.repository.AdRepository;
import com.api.udc.post.repository.FreeRepository;
import com.api.udc.post.repository.PostRepository;
import com.api.udc.post.repository.QARepository;
import com.api.udc.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final QARepository qaRepository;
    private final FreeRepository freeRepository;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<CustomApiResponse<?>> addComment(CommentDto commentDto) {
        Optional<Post> optionalPost = findPostById(commentDto.getId());

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Comment comment = new Comment(commentDto.getCommenter(), commentDto.getContent());
            post.addComment(comment);
            commentRepository.save(comment);
            CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "댓글작성이 완료되었습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "게시물을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    private Optional<Post> findPostById(Long postId) {
        return Optional.ofNullable(
                qaRepository.findById(postId)
                        .orElse(freeRepository.findById(postId)
                                .orElse(adRepository.findById(postId).orElse(null)))
        );
    }

    public ResponseEntity<CustomApiResponse<?>> deleteComment(Long postId, Long commentId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Optional<Comment> optionalComment = commentRepository.findById(commentId);

            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();

                // postId와 comment의 post id가 같은지 확인
                if (comment.getPost().getId().equals(post.getId())) {
                    commentRepository.delete(comment);
                    CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "댓글이 삭제되었습니다.");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            }
        }

        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "게시물 또는 댓글을 찾을 수 없습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
