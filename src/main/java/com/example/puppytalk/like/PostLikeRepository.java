package com.example.puppytalk.like;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndPost(User user, Post post);

    long countByPost(Post post);
    void deleteByUserAndPost(User user, Post post);

    @Query("SELECT pl.post.id FROM PostLike pl WHERE pl.user = :user AND pl.post IN :posts")
    Set<Long> findLikedPostIdsByUserAndPosts(@Param("user") User user, @Param("posts") List<Post> posts);
}
