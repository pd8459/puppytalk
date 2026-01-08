package com.example.puppytalk.Shop;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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
}