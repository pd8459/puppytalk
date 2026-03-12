package com.example.puppytalk.Shop;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByStatus(ProductStatus status);
    List<Product> findByTargetBreedAndStatus(String targetBreed, ProductStatus status);
    List<Product> findByRecommendedSizeAndStatus(DogSize recommendedSize, ProductStatus status);
    List<Product> findAllByStatusOrderByCreatedAtDesc(ProductStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.id = :id AND p.stockQuantity >= :quantity")
    int decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);

    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.category",
            countQuery = "SELECT count(p) FROM Product p")
    Page<Product> findAllWithCategory(Pageable pageable);
}