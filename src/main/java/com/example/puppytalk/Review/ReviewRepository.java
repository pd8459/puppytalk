package com.example.puppytalk.Review;

import com.example.puppytalk.Shop.Product;
import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT r FROM Review r JOIN FETCH r.user WHERE r.product = :product ORDER BY r.createdAt DESC",
            countQuery = "SELECT count(r) FROM Review r WHERE r.product = :product")
    Page<Review> findAllByProductOrderByCreatedAtDesc(@Param("product") Product product, Pageable pageable);

    @Query(value = "SELECT r FROM Review r JOIN FETCH r.product WHERE r.user = :user ORDER BY r.createdAt DESC",
            countQuery = "SELECT count(r) FROM Review r WHERE r.user = :user")
    Page<Review> findAllByUserOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);

    boolean existsByUserAndProduct(User user, Product product);

    @Query("select avg(r.rating) from Review r where r.product.id = :productId")
    Double getAverageRating(@Param("productId") Long productId);

}

