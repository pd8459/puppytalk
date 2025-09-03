package com.example.puppytalk.Comment;

import com.example.puppytalk.User.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String authorNickname;
    private String authorProfileImageUrl;
    private String authorUsername;
    private LocalDateTime createdAt;
    private long likeCount;
    private boolean liked;
    private Long postId;
    private List<CommentResponseDto> children = new ArrayList<>();

    public CommentResponseDto(Comment comment, long likeCount, boolean liked) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorNickname = comment.getUser().getNickname();
        this.authorProfileImageUrl = comment.getUser().getProfileImageUrl();
        this.authorUsername = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = likeCount;
        this.liked = liked;
        this.postId = comment.getPost().getId();

        User author = comment.getUser();
        if (author.isDeleted()) {
            this.authorNickname = "탈퇴한 사용자";
            this.authorProfileImageUrl = "/images/default_profile.png";
        } else {
            this.authorNickname = author.getNickname();
            this.authorProfileImageUrl = author.getProfileImageUrl();
        }
    }
    public void setChildren(List<CommentResponseDto> children) {
        this.children = children;
    }
}
