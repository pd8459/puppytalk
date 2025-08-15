package com.example.puppytalk.Comment;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
    private long likeCount;
    private boolean liked;
    private List<CommentResponseDto> children = new ArrayList<>();

    public CommentResponseDto(Comment comment, long likeCount, boolean liked) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorNickname = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = likeCount;
        this.liked = liked;
    }
    public void setChildren(List<CommentResponseDto> children) {
        this.children = children;
    }
}
