package com.example.puppytalk.Post;

import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {


    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user LEFT JOIN FETCH p.images",
            countQuery = "SELECT count(p) FROM Post p")
    Page<Post> findAllWithUserAndImages(Pageable pageable);


    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user LEFT JOIN FETCH p.images WHERE p.category = :category",
            countQuery = "SELECT count(p) FROM Post p WHERE p.category = :category")
    Page<Post> findByCategoryWithUserAndImages(@Param("category") PostCategory category, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.title LIKE %:keyword%",
            countQuery = "SELECT count(p) FROM Post p WHERE p.title LIKE %:keyword%")
    Page<Post> findByTitleContainingWithUser(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.title LIKE %:keyword% AND p.category = :category",
            countQuery = "SELECT count(p) FROM Post p WHERE p.title LIKE %:keyword% AND p.category = :category")
    Page<Post> findByTitleContainingAndCategoryWithUser(@Param("keyword") String keyword, @Param("category") PostCategory category, Pageable pageable);

    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    Page<Post> findAllByUser(User user, Pageable pageable);
}