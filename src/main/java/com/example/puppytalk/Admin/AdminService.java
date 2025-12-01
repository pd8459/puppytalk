package com.example.puppytalk.Admin;

import com.example.puppytalk.Shop.*;
import com.example.puppytalk.Shop.OrderHistoryDto;
import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRepository;
import com.example.puppytalk.User.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public AdminDashboardDto getDashboardStats() {
        AdminDashboardDto dto = new AdminDashboardDto();

        Long sales = paymentRepository.getTotalSales();
        dto.setTotalSales(sales == null ? 0 : sales);

        dto.setTotalOrders(orderRepository.count());
        dto.setTotalMembers(userRepository.count());

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getAllOrders(OrderStatus status, Pageable pageable) {
        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findAllByStatusOrderByOrderDateDesc(status, pageable);
        } else {
            orders = orderRepository.findAllByOrderByOrderDateDesc(pageable);
        }
        return orders.map(OrderHistoryDto::new);
    }

    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));
        order.updateStatus(newStatus);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.updateStatus(status);
    }
}