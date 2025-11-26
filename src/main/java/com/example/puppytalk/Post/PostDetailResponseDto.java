package com.example.puppytalk.Post;

import com.example.puppytalk.Comment.CommentResponseDto;
import com.example.puppytalk.User.User;
import com.example.puppytalk.image.Image;
import com.example.puppytalk.like.CommentLikeRepository;
import com.example.puppytalk.like.PostLikeRepository;
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

        public PostDetailResponseDto(Post post, User user, PostLikeRepository postLikeRepo, CommentLikeRepository commentLikeRepo) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.authorNickname = post.getUser() != null ? post.getUser().getNickname() : "알 수 없는 사용자";
            this.createdAt = post.getCreatedAt();
            this.likeCount = postLikeRepo.countByPost(post);
            this.isLiked = user != null && postLikeRepo.findByUserAndPost(user, post).isPresent();

            this.imageUrls = post.getImages().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());

            this.comments = post.getComments().stream()
                    .map(comment -> new CommentResponseDto(
                            comment,
                            commentLikeRepo.countByComment(comment),
                            user != null && commentLikeRepo.findByUserAndComment(user,comment).isPresent()
                    )).collect(Collectors.toList());
        }

}
