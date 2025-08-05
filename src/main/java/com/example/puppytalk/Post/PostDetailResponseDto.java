package com.example.puppytalk.Post;

import com.example.puppytalk.Comment.CommentResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private List<CommentResponseDto> comments;
    private long likeCount;
    private boolean isLiked;

        public PostDetailResponseDto(Post post, long likeCount, boolean isLiked) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.authorNickname = post.getUser().getNickname();
            this.createdAt = post.getCreatedAt();
            this.likeCount = likeCount;
            this.isLiked = isLiked;

            if (post.getUser() != null) {
                this.authorNickname = post.getUser().getNickname();
            } else {
                this.authorNickname = "알 수 없는 사용자";
            }

            this.imageUrls = post.getImages().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());

            this.comments = post.getComments().stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }

}
