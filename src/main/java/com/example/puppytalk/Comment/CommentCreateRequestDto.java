package com.example.puppytalk.Comment;

import lombok.Getter;

@Getter
public class CommentCreateRequestDto extends CommentRequestDto {
    private Long postId;
}
