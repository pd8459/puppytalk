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
    private final ImageRepository imageRepository;
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

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);

        if(!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto requestDto, User user) {
        Post post = findPost(postId);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        List<Image> existingImages = post.getImages();
        String content = requestDto.getContent();

        List<Image> imagesToDelete = existingImages.stream()
                .filter(image -> !content.contains(image.getImageUrl()))
                .toList();

        for (Image image : imagesToDelete) {
            imageRepository.delete(image);
        }

        post.setTitle(requestDto.getTitle());
        post.setContent(content);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
    }
}
