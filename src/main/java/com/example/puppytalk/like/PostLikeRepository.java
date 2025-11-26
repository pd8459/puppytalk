package com.example.puppytalk.like;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndPost(User user, Post post);

    long countByPost(Post post);
    void deleteByUserAndPost(User user, Post post);
}
