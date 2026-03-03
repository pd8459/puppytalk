package com.example.puppytalk.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p JOIN FETCH p.order WHERE p.impUid = :impUid")
    Optional<Payment> findByImpUidWithOrder(@Param("impUid") String impUid);

    @Query("SELECT p FROM Payment p JOIN FETCH p.order WHERE p.order = :order")
    Optional<Payment> findByOrderWithOrder(@Param("order") Order order);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.order.status <> 'CANCEL'")
    Long getTotalSales();
}