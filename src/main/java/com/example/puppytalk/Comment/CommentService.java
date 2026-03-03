package com.example.puppytalk.Comment;

import com.example.puppytalk.Config.RestPage;
import com.example.puppytalk.Notification.NotificationService;
import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostRepository;
import com.example.puppytalk.User.User;
import com.example.puppytalk.like.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final NotificationService notificationService;

    @Transactional
    @CacheEvict(value = "postComments", key = "#postId + ':0'")
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
    @CacheEvict(value = "postComments", key = "#postId + ':0'")
    public CommentResponseDto createReply(Long postId, Long parentId, CommentCreateRequestDto requestDto, User user) {
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("원본 댓글을 찾을 수 없습니다."));

        Post post = parentComment.getPost();
        Comment reply = new Comment(requestDto.getContent(), user, post, parentComment);
        Comment savedReply = commentRepository.save(reply);
        notificationService.sendNewReplyOnCommentNotification(savedReply);

        return new CommentResponseDto(savedReply, 0L, false);
    }

    @Transactional
    @CacheEvict(value = "postComments", key = "#postId + ':0'")
    public void updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findByIdWithUser(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }
        comment.setContent(requestDto.getContent());
    }

    @Transactional
    @CacheEvict(value = "postComments", key = "#postId + ':0'")
    public void deleteComment(Long postId, Long commentId, User user) {
        Comment comment = commentRepository.findByIdWithUser(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postComments", key = "#postId + ':' + #page")
    public Page<CommentResponseDto> getComments(Long postId, int page, int size, User currentUser) {
        Post post = postRepository.findById(postId).orElseThrow();
        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> commentPage = commentRepository.findByPostAndParentIsNullOrderByCreatedAtAsc(post, pageable);

        if (commentPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> commentIds = commentPage.getContent().stream().map(Comment::getId).collect(Collectors.toList());
        Map<Long, Long> commentLikeMap = commentLikeRepository.countLikesByCommentIds(commentIds)
                .stream()
                .collect(Collectors.toMap(obj -> (Long) obj[0], obj -> (Long) obj[1]));
        Set<Long> currentUserLikes = (currentUser != null)
                ? commentLikeRepository.findLikedCommentIdsByUser(commentIds, currentUser)
                : Collections.emptySet();

        List<CommentResponseDto> dtoList = commentPage.getContent().stream()
                .map(comment -> {
                    long likeCount = commentLikeMap.getOrDefault(comment.getId(), 0L);
                    boolean isLiked = currentUserLikes.contains(comment.getId());
                    return new CommentResponseDto(comment, likeCount, isLiked);
                })
                .collect(Collectors.toList());

        return new RestPage<>(dtoList, pageable.getPageNumber(), pageable.getPageSize(), commentPage.getTotalElements());
    }

    private CommentResponseDto convertToDto(Comment comment, Set<Long> likedCommentIds) {
        CommentResponseDto dto = new CommentResponseDto(
                comment,
                (long) comment.getLikes().size(),
                likedCommentIds.contains(comment.getId())
        );

        List<CommentResponseDto> childrenDtos = comment.getChildren().stream()
                .map(child -> convertToDto(child, likedCommentIds))
                .collect(Collectors.toList());
        dto.setChildren(childrenDtos);

        return dto;
    }
}