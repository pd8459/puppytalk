package com.example.puppytalk.Post;

import com.example.puppytalk.User.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String authorNickname;
    private LocalDateTime createdAt;
    private String imageUrl;
    private String authorProfileImageUrl;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.authorNickname = post.getUser().getNickname();
        this.authorProfileImageUrl = post.getUser().getProfileImageUrl();
        this.createdAt = post.getCreatedAt();
        if(post.getImages() != null && !post.getImages().isEmpty()) {
            this.imageUrl = post.getImages().get(0).getImageUrl();
        } else {
            this.imageUrl = null;
        }
        User author = post.getUser();
        if (author.isDeleted()) {
            this.authorNickname = "탈퇴한 사용자";
            this.authorProfileImageUrl = "/images/default_profile.png";
        } else {
            this.authorNickname = author.getNickname();
            this.authorProfileImageUrl = author.getProfileImageUrl();
        }

        if (post.getImages() != null && !post.getImages().isEmpty()) {
            this.imageUrl = post.getImages().get(0).getImageUrl();
        } else {
            this.imageUrl = null;
        }
    }
}
