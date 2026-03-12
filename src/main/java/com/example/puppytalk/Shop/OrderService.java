package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final ProductRepository productRepository;

    public Long orderFromCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Long productId = cartItem.getProduct().getId();
            int count = cartItem.getCount();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            boolean isTimeDeal = product.getSaleStartTime() != null;

            if (isTimeDeal) {
                Product lockedProduct = productRepository.findByIdWithLock(productId)
                        .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

                if (lockedProduct.getStockQuantity() < count) {
                    throw new IllegalArgumentException("재고가 부족합니다.");
                }

                orderItems.add(OrderItem.createOrderItem(lockedProduct, lockedProduct.getCurrentPrice(), count));
            } else {
                int updatedCount = productRepository.decreaseStock(productId, count);
                if (updatedCount == 0) {
                    throw new IllegalArgumentException("재고가 부족합니다.");
                }

                Product updatedProduct = productRepository.findById(productId).get();
                if (updatedProduct.getStockQuantity() == 0) {
                    updatedProduct.changeStatus(ProductStatus.SOLD_OUT);
                }

                orderItems.add(OrderItem.createNormalOrderItem(updatedProduct, updatedProduct.getCurrentPrice(), count));
            }
        }

        Order order = Order.createOrder(user, orderItems);
        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);
        cart.getItems().clear();

        return order.getId();
    }

    public void cancelOrder(Long orderId, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        if (!order.getUser().getUsername().equals(username)) {
            throw new RuntimeException("주문 취소 권한이 없습니다.");
        }

        if (order.getStatus() == OrderStatus.CANCEL) {
            throw new RuntimeException("이미 취소된 주문입니다.");
        }

        Payment payment = paymentRepository.findByOrderWithOrder(order).orElse(null);

        if (payment != null && "PAID".equals(payment.getStatus())) {
            try {
                paymentService.cancelPayment(payment.getImpUid());
            } catch (Exception e) {
                throw new RuntimeException("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
            }
        }

        order.cancel();
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryDto> getOrderList(User user) {
        return orderRepository.findAllByUserOrderByOrderDateDesc(user).stream()
                .map(OrderHistoryDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Order getOrderEntity(Long orderId) {
        return orderRepository.findOrderWithItemsAndProductsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryDto> getAllOrdersForAdmin() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return orders.stream()
                .map(OrderHistoryDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String statusStr) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        try {
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());

            if (order.getStatus() == OrderStatus.CANCEL) {
                throw new IllegalStateException("취소된 주문은 상태를 변경할 수 없습니다.");
            }

            order.setStatus(status);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 상태 값입니다: " + statusStr);
        }
    }

    @Transactional
    public void requestRefund(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 주문만 환불 요청할 수 있습니다.");
        }

        if (order.getStatus() == OrderStatus.CANCEL || order.getStatus() == OrderStatus.CANCEL_REQUESTED) {
            throw new IllegalArgumentException("이미 취소되었거나 환불 요청된 주문입니다.");
        }

        order.setStatus(OrderStatus.CANCEL_REQUESTED);
    }

    public Long directOrder(User user, Long productId, int count) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        boolean isTimeDeal = product.getSaleStartTime() != null;

        List<OrderItem> orderItems = new ArrayList<>();

        if (isTimeDeal) {
            Product lockedProduct = productRepository.findByIdWithLock(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            if (lockedProduct.getStockQuantity() < count) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }

            orderItems.add(OrderItem.createOrderItem(lockedProduct, lockedProduct.getCurrentPrice(), count));
        } else {
            int updatedCount = productRepository.decreaseStock(productId, count);
            if (updatedCount == 0) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }

            Product updatedProduct = productRepository.findById(productId).get();
            if (updatedProduct.getStockQuantity() == 0) {
                updatedProduct.changeStatus(ProductStatus.SOLD_OUT);
            }

            orderItems.add(OrderItem.createNormalOrderItem(updatedProduct, updatedProduct.getCurrentPrice(), count));
        }

        Order order = Order.createOrder(user, orderItems);
        orderRepository.save(order);
        return order.getId();
    }
}