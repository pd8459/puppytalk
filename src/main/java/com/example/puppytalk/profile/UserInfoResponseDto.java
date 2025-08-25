package com.example.puppytalk.profile;

import com.example.puppytalk.User.User;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String username;
    private String nickname;
    private String profileImageUrl;

    public UserInfoResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.profileImageUrl = user.getProfileImageUrl();
    }
}
