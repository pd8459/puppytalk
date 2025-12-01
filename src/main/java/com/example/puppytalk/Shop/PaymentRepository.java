package com.example.puppytalk.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByImpUid(String impUid);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.order.status <> 'CANCEL'")
    Long getTotalSales();
}
