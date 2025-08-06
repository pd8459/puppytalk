package com.example.puppytalk.like;

import com.example.puppytalk.Comment.Comment;
import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user,  Comment comment);
    long countByComment(Comment comment);

    @Transactional
    void deleteByUserAndComment(User user, Comment comment);
}
