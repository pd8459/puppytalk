package com.example.puppytalk.Comment;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostRepository;
import com.example.puppytalk.User.User;
import com.example.puppytalk.like.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );

        Comment comment = new Comment(requestDto.getContent(), user, post, null);
        commentRepository.save(comment);
    }

    @Transactional
    public void createReply(Long parentId, CommentRequestDto requestDto, User user) {
        Comment parentComment = findComment(parentId);
        Post post = parentComment.getPost();

        Comment reply = new Comment(requestDto.getContent(), user, post, parentComment);
        commentRepository.save(reply);
    }

    @Transactional
    public void updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }
        comment.setContent(requestDto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getCommentsByPost(Long postId, User user, Pageable pageable) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        Page<Comment> comments = commentRepository.findByPostAndParentIsNullOrderByCreatedAtAsc(post, pageable);
        return comments.map(comment -> {
            CommentResponseDto parentDto = new CommentResponseDto(
                    comment,
                    commentLikeRepository.countByComment(comment),
                    user != null && commentLikeRepository.findByUserAndComment(user, comment).isPresent()
            );
            List<CommentResponseDto> childDtos = comment.getChildren().stream()
                    .map(child -> new CommentResponseDto(
                            child,
                            commentLikeRepository.countByComment(child),
                            user != null && commentLikeRepository.findByUserAndComment(user, child).isPresent()
                    ))
                    .collect(Collectors.toList());

            parentDto.setChildren(childDtos);

            return parentDto;
        });
    }
}
