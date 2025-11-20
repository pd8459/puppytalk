package com.example.puppytalk.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByStatus(ProductStatus status);
    List<Product> findByTargetBreedAndStatus(String targetBreed, ProductStatus status);
    List<Product> findByRecommendedSizeAndStatus(DogSize recommendedSize, ProductStatus status);
    List<Product> findAllByStatusOrderByCreatedAtDesc(ProductStatus status);
}