package com.example.puppytalk.like;

import com.example.puppytalk.Comment.Comment;
import com.example.puppytalk.Post.Post;
import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user,  Comment comment);
    long countByComment(Comment comment);

    @Transactional
    void deleteByUserAndComment(User user, Comment comment);

    @Query("SELECT cl.comment.id FROM CommentLike cl WHERE cl.user = :user AND cl.comment.post = :post")
    Set<Long> findLikedCommentIdsByUserAndPost(@Param("user") User user, @Param("post") Post post);

    @Query("SELECT cl.comment.id, COUNT(cl) FROM CommentLike cl " +
            "WHERE cl.comment.id IN :commentIds " +
            "GROUP BY cl.comment.id")
    List<Object[]> countLikesByCommentIds(@Param("commentIds") List<Long> commentIds);

    @Query("SELECT cl.comment.id FROM CommentLike cl " +
            "WHERE cl.comment.id IN :commentIds AND cl.user = :user")
    Set<Long> findLikedCommentIdsByUser(@Param("commentIds") List<Long> commentIds, @Param("user") User user);
}
