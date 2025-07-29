package com.example.puppytalk.Post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String authorNickname;
    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.authorNickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
    }
}
