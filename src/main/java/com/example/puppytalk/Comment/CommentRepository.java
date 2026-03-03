package com.example.puppytalk.Comment;

import com.example.puppytalk.Post.Post;
import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c FROM Comment c " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.post " +
            "WHERE c.post = :post AND c.parent IS NULL " +
            "ORDER BY c.createdAt ASC",
            countQuery = "SELECT count(c) FROM Comment c WHERE c.post = :post AND c.parent IS NULL")
    Page<Comment> findByPostAndParentIsNullOrderByCreatedAtAsc(@Param("post") Post post, Pageable pageable);

    @Query(value = "SELECT c FROM Comment c " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.post " +
            "WHERE c.user = :user " +
            "ORDER BY c.createdAt DESC",
            countQuery = "SELECT count(c) FROM Comment c WHERE c.user = :user")
    Page<Comment> findAllByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Comment> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT l.comment.id, COUNT(l) FROM CommentLike l WHERE l.comment.post = :post GROUP BY l.comment.id")
    List<Object[]> countLikesByPost(@Param("post") Post post);
}