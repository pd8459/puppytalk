package com.example.puppytalk.Review;

import com.example.puppytalk.Shop.Product;
import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByProductOrderByCreatedAtDesc(Product product, Pageable pageable);

    Page<Review> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    boolean existsByUserAndProduct(User user, Product product);

    @Query("select avg(r.rating) from Review r where r.product.id = :productId")
    Double getAverageRating(@Param("productId") Long productId);

}

