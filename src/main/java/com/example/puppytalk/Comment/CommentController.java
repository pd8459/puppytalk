package com.example.puppytalk.Comment;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<String> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.createComment(postId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 성공적으로 작성되었습니다.");
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateComment(commentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }
}
