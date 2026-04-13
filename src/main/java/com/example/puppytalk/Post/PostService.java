package com.example.puppytalk.Post;

import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRole;
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
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPosts(PostCategory category, Pageable pageable) {
        Page<Post> posts;
        if (category != null) {
            posts = postRepository.findByCategoryWithUser(category, pageable);
        } else {
            posts = postRepository.findAllWithUser(pageable);
        }
        return posts.map(PostResponseDto::new);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id, User currentUser) {
        Post post = postRepository.findByIdWithUser(id).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );

        long likeCount = post.getLikes().size();
        boolean isLiked = false;

        if (currentUser != null) {
            isLiked = postLikeRepository.findByUserAndPost(currentUser, post).isPresent();
        }

        return new PostResponseDto(post, likeCount, isLiked);
    }

    @Transactional
    public Post createPost(PostRequestDto requestDto, User user) {
        Post post = new Post();
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setCategory(requestDto.getCategory());
        post.setUser(user);
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post = postRepository.findByIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto requestDto, User user) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        List<Image> existingImages = post.getImages();
        String content = requestDto.getContent();

        List<String> imageUrlsToDelete = existingImages.stream()
                .map(Image::getImageUrl)
                .filter(url -> !content.contains(url))
                .toList();

        if (!imageUrlsToDelete.isEmpty()) {
            imageRepository.deleteAllByImageUrlIn(imageUrlsToDelete);
        }

        post.setTitle(requestDto.getTitle());
        post.setContent(content);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> searchPosts(String keyword, PostCategory category, Pageable pageable) {
        Page<Post> posts;
        if (category != null) {
            posts = postRepository.findByTitleContainingAndCategoryWithUser(keyword, category, pageable);
        } else {
            posts = postRepository.findByTitleContainingWithUser(keyword, pageable);
        }
        return posts.map(PostResponseDto::new);
    }
}