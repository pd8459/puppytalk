package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserOrderByOrderDateDesc(User user);
}