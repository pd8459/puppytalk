package com.example.puppytalk.User;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PublicProfileResponseDto {
    private String nickname;
    private String profileImageUrl;
    private Page<PostResponseDto> posts;

    public PublicProfileResponseDto(User user, Page<Post> postPage) {
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
        this.posts = postPage.map(PostResponseDto::new);
    }
}
