package com.example.puppytalk.Shop;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> orderFromCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long orderId = orderService.orderFromCart(userDetails.getUser());
        return ResponseEntity.ok("주문이 완료되었습니다! 주문번호: " + orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderHistoryDto>> getOrderList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(orderService.getOrderList(userDetails.getUser()));
    }
}