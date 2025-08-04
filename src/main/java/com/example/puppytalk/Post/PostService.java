package com.example.puppytalk.Post;

import com.example.puppytalk.FileUploadService;
import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        return new PostDetailResponseDto(post);
    }

    @Transactional
    public void createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), user);
        postRepository.save(post);
    }

}
