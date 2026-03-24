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
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public AdminDashboardDto getDashboardStats() {
        long totalSales = 0;
        List<Order> orders = orderRepository.findAll();

        long cancelCount = 0;

        for (Order order : orders) {
            if (order.getStatus() != OrderStatus.CANCEL) {
                totalSales += order.getTotalPrice();
            }
            if (order.getStatus() == OrderStatus.CANCEL || order.getStatus() == OrderStatus.CANCEL_REQUESTED) {
                cancelCount++;
            }
        }

        long totalOrders = orders.size();
        long totalMembers = userRepository.count();

        return new AdminDashboardDto(totalSales, totalOrders, totalMembers, cancelCount);
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

    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getCancelRequests(Pageable pageable) {
        return orderRepository.findAllByStatus(OrderStatus.CANCEL_REQUESTED, pageable)
                .map(OrderHistoryDto::new);
    }

    @Transactional
    public void approveCancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        order.setStatus(OrderStatus.CANCEL);

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getCount());
        }

    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    @Transactional
    public void updateProduct(Long productId, AdminController.ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        product.updateProduct(
                request.getName(),
                request.getOriginalPrice(),
                request.getDiscountRate(),
                request.getSalePrice(),
                request.getDescription(),
                request.getThumbnailUrl(),
                request.getStockQuantity(),
                request.getTargetBreed(),
                request.getRecommendedSize(),
                request.getSaleStartTime(),
                request.getSaleEndTime()
        );

        if (request.getStatus() != null) {
            product.changeStatus(request.getStatus());
        }
    }

}