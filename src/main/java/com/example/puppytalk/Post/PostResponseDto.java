package com.example.puppytalk.Post;

import com.example.puppytalk.image.Image;
import com.example.puppytalk.User.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private String authorProfileImageUrl;
    private LocalDateTime createdAt;
    private String imageUrl;
    private long likeCount;
    private boolean isLiked;
    private PostCategory category;
    private String categoryDescription;
    private String authorUsername;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorUsername = post.getUser().getUsername();

        User author = post.getUser();
        if (author.isDeleted()) {
            this.authorNickname = "탈퇴한 사용자";
            this.authorProfileImageUrl = "/images/default_profile.png";
        } else {
            this.authorNickname = author.getNickname();
            this.authorProfileImageUrl = author.getProfileImageUrl();
        }

        this.createdAt = post.getCreatedAt();
        List<Image> images = post.getImages();
        if (images != null && !images.isEmpty()) {
            this.imageUrl = images.get(0).getImageUrl();
        }
        this.category = post.getCategory();
        if (post.getCategory() != null) {
            this.categoryDescription = post.getCategory().getDescription();
        }
    }

    public PostResponseDto(Post post, long likeCount, boolean isLiked) {
        this(post);
        this.likeCount = likeCount;
        this.isLiked = isLiked;

    }
}