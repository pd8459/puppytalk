package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserOrderByOrderDateDesc(User user);
    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
    Page<Order> findAllByStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    @Query("select count(o) > 0 " +
            "from Order o " +
            "join o.orderItems oi " +
            "where o.user.id = :userId " +
            "and oi.product.id = :productId " +
            "and o.status != 'CANCEL'")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}