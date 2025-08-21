package com.example.puppytalk.Mypage;

import com.example.puppytalk.Comment.Comment;
import com.example.puppytalk.Comment.CommentRepository;
import com.example.puppytalk.Comment.CommentResponseDto;
import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostRepository;
import com.example.puppytalk.Post.PostResponseDto;
import com.example.puppytalk.User.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getMyPosts(User user, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByUser(user, pageable);
        return posts.map(PostResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getMyComments(User user, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByUser(user, pageable);
        return comments.map(comment -> new CommentResponseDto(comment, 0L, false));
    }
}