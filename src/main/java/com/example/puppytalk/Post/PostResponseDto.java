package com.example.puppytalk.Post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String authorNickname;
    private LocalDateTime createdAt;
    private String imageUrl;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.authorNickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        if(post.getImages() != null && !post.getImages().isEmpty()) {
            this.imageUrl = post.getImages().get(0).getImageUrl();
        } else {
            this.imageUrl = null;
        }
    }
}
