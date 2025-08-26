package com.example.puppytalk.Post;

import com.example.puppytalk.FileUploadService;
import com.example.puppytalk.User.User;
import com.example.puppytalk.image.Image;
import com.example.puppytalk.image.ImageRepository;
import com.example.puppytalk.like.CommentLikeRepository;
import com.example.puppytalk.like.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FileUploadService fileUploadService;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(PostResponseDto::new);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id, User currentUser) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );

        long likeCount = post.getLikes().size();
        boolean isLiked = false;
        if (currentUser != null) {
            isLiked = post.getLikes().stream()
                    .anyMatch(postLike -> postLike.getUser().getId().equals(currentUser.getId()));
        }

        return new PostResponseDto(post, likeCount, isLiked);
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

    @Transactional(readOnly = true)
    public Page<PostResponseDto> searchPosts(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleContaining(keyword, pageable);
        return posts.map(PostResponseDto::new);
    }
}
