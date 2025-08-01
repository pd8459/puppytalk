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
    private List<CommentResponseDto> comments;

        public PostDetailResponseDto(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.authorNickname = post.getUser().getNickname();
            this.createdAt = post.getCreatedAt();
            this.comments = post.getComments().stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }

}
