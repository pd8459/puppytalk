package com.example.puppytalk.Post;

import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
    Page<Post> findAllByUser(User user, Pageable pageable);
    Page<Post> findByCategory(PostCategory category, Pageable pageable);
    Page<Post> findByTitleContainingAndCategory(String title, PostCategory category, Pageable pageable);

}
