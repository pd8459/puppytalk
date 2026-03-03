package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.user " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product " +
            "WHERE o.user = :user ORDER BY o.orderDate DESC")
    List<Order> findAllByUserOrderByOrderDateDesc(@Param("user") User user);

    @Query(value = "SELECT o FROM Order o JOIN FETCH o.user ORDER BY o.orderDate DESC",
            countQuery = "SELECT count(o) FROM Order o")
    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);

    @Query(value = "SELECT o FROM Order o JOIN FETCH o.user WHERE o.status = :status ORDER BY o.orderDate DESC",
            countQuery = "SELECT count(o) FROM Order o WHERE o.status = :status")
    Page<Order> findAllByStatusOrderByOrderDateDesc(@Param("status") OrderStatus status, Pageable pageable);

    @Query(value = "SELECT o FROM Order o JOIN FETCH o.user WHERE o.status = :status",
            countQuery = "SELECT count(o) FROM Order o WHERE o.status = :status")
    Page<Order> findAllByStatus(@Param("status") OrderStatus status, Pageable pageable);

    @Query("select count(o) > 0 " +
            "from Order o " +
            "join o.orderItems oi " +
            "where o.user.id = :userId " +
            "and oi.product.id = :productId " +
            "and o.status != 'CANCEL'")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product " +
            "WHERE o.id = :orderId")
    Optional<Order> findOrderWithItemsAndProductsById(@Param("orderId") Long orderId);
}