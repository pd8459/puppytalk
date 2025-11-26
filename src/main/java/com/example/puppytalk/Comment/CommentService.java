package com.example.puppytalk.Comment;

import com.example.puppytalk.Notification.NotificationService;
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
    private final NotificationService notificationService;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );
        Comment comment = new Comment(requestDto.getContent(), user, post, null);
        Comment savedComment = commentRepository.save(comment);
        notificationService.sendNewCommentOnPostNotification(savedComment);

        return new CommentResponseDto(savedComment, 0L, false);
    }

    @Transactional
    public CommentResponseDto createReply(Long parentId, CommentCreateRequestDto requestDto, User user) {
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("원본 댓글을 찾을 수 없습니다."));

        Post post = parentComment.getPost();
        Comment reply = new Comment(requestDto.getContent(), user, post, parentComment);
        Comment savedReply = commentRepository.save(reply);
        notificationService.sendNewReplyOnCommentNotification(savedReply);

        return new CommentResponseDto(savedReply, 0L, false);
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
        return comments.map(comment -> convertToDto(comment, user));
    }

    private CommentResponseDto convertToDto(Comment comment, User user) {
        CommentResponseDto dto = new CommentResponseDto(
                comment,
                commentLikeRepository.countByComment(comment),
                user != null && commentLikeRepository.findByUserAndComment(user, comment).isPresent()
        );
        List<CommentResponseDto> childrenDtos = comment.getChildren().stream()
                .map(child -> convertToDto(child, user))
                .collect(Collectors.toList());
        dto.setChildren(childrenDtos);

        return dto;
    }
}
