package com.example.puppytalk.Shop;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ProductOption p where p.id = :id")
    Optional<ProductOption> findByIdWithLock(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update ProductOption p set p.stockQuantity = p.stockQuantity - :count where p.id = :id and p.stockQuantity >= :count")
    int decreaseStock(@Param("id") Long id, @Param("count") int count);
}