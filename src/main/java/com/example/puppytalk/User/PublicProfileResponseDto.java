package com.example.puppytalk.User;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostResponseDto;
import com.example.puppytalk.pet.PetResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PublicProfileResponseDto {
    private String nickname;
    private String profileImageUrl;
    private Page<PostResponseDto> posts;
    private List<PetResponseDto> pets;

    public PublicProfileResponseDto(User user, Page<Post> postPage) {
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
        this.posts = postPage.map(PostResponseDto::new);
        this.pets = user.getPets().stream()
                .map(PetResponseDto::new)
                .collect(Collectors.toList());
    }
}
