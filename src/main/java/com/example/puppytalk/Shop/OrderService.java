package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import lombok.RequiredArgsConstructor;
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

    public Long orderFromCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.createOrderItem(
                    cartItem.getProduct(),
                    cartItem.getProduct().getCurrentPrice(),
                    cartItem.getCount()
            );
            orderItems.add(orderItem);
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

        Payment payment = paymentRepository.findByOrder(order).orElse(null);

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
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}