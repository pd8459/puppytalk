package com.example.puppytalk.Comment;

import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserDetailsImpl;
import com.example.puppytalk.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentResponseDto responseDto = commentService.createComment(postId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/api/posts/{postId}/comments/{parentId}/replies")
    public ResponseEntity<CommentResponseDto> createReply(
            @PathVariable Long postId,
            @PathVariable Long parentId,
            @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentResponseDto responseDto = commentService.createReply(postId, parentId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateComment(postId, commentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.deleteComment(postId, commentId, userDetails.getUser());
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/api/comments/{commentId}/like")
    public ResponseEntity<String> toggleCommentLike(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean liked = likeService.toggleCommentLike(commentId, userDetails.getUser());

        if (liked) {
            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        } else {
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        }
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User currentUser = (userDetails != null) ? userDetails.getUser() : null;
        return ResponseEntity.ok(commentService.getComments(postId, page, size, currentUser));
    }
}