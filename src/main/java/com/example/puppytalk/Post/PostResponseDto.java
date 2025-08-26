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

    // 1. 게시글 '목록'을 위한 생성자
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

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
    }

    // 2. 게시글 '상세' 조회를 위한 생성자 (좋아요 정보 추가)
    public PostResponseDto(Post post, long likeCount, boolean isLiked) {
        this(post); // 기본 생성자를 먼저 호출해서 중복 코드 제거
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }
}