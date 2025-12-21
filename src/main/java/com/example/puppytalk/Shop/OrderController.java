package com.example.puppytalk.Shop;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> orderFromCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long orderId = orderService.orderFromCart(userDetails.getUser());
        Order order = orderService.getOrderEntity(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("totalPrice", order.getTotalPrice());
        response.put("orderName", order.getOrderItems().get(0).getProduct().getName() + " 외 " + (order.getOrderItems().size() - 1) + "건");
        response.put("buyerName", userDetails.getUser().getNickname());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.cancelOrder(orderId, userDetails.getUsername());

        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderHistoryDto>> getOrderList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(orderService.getOrderList(userDetails.getUser()));
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<String> requestRefund(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.requestRefund(orderId, userDetails.getUser());
        return ResponseEntity.ok("환불 요청이 접수되었습니다. 관리자 승인 후 처리됩니다.");
    }
}