package com.example.puppytalk.Post;

import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional; // 👈 단건 조회를 위해 꼭 필요합니다!

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user",
            countQuery = "SELECT count(p) FROM Post p")
    Page<Post> findAllWithUser(Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.category = :category",
            countQuery = "SELECT count(p) FROM Post p WHERE p.category = :category")
    Page<Post> findByCategoryWithUser(@Param("category") PostCategory category, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.title LIKE %:keyword%",
            countQuery = "SELECT count(p) FROM Post p WHERE p.title LIKE %:keyword%")
    Page<Post> findByTitleContainingWithUser(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.title LIKE %:keyword% AND p.category = :category",
            countQuery = "SELECT count(p) FROM Post p WHERE p.title LIKE %:keyword% AND p.category = :category")
    Page<Post> findByTitleContainingAndCategoryWithUser(@Param("keyword") String keyword, @Param("category") PostCategory category, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") Long id);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.user = :user ORDER BY p.createdAt DESC",
            countQuery = "SELECT count(p) FROM Post p WHERE p.user = :user")
    Page<Post> findByUserOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.user = :user",
            countQuery = "SELECT count(p) FROM Post p WHERE p.user = :user")
    Page<Post> findAllByUser(@Param("user") User user, Pageable pageable);
}