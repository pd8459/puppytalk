package com.example.puppytalk.Post;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String content;
    private PostCategory category;
}
