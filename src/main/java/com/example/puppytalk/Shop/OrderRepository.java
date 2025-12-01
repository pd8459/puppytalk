package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserOrderByOrderDateDesc(User user);
    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);
    Page<Order> findAllByStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);}