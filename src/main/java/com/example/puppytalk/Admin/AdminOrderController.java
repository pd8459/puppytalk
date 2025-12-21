package com.example.puppytalk.Admin;

import com.example.puppytalk.Shop.OrderHistoryDto;
import com.example.puppytalk.Shop.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public String orderListPage(Model model) {
        List<OrderHistoryDto> orders = orderService.getAllOrdersForAdmin();
        model.addAttribute("orders", orders);
        return "admin/order-list";
    }

    @PostMapping("/{orderId}/status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@PathVariable Long orderId, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("상태가 변경되었습니다.");
    }

    @PostMapping("/orders/bulk-status")
    public ResponseEntity<String> updateBulkStatus(@RequestBody Map<String, Object> body) {
        List<Integer> orderIdsInt = (List<Integer>) body.get("orderIds");
        String statusStr = (String) body.get("status");
        List<Long> orderIds = orderIdsInt.stream().map(Long::valueOf).toList();

        for (Long id : orderIds) {
            orderService.updateOrderStatus(id, statusStr);
        }

        return ResponseEntity.ok(orderIds.size() + "건의 주문 상태가 변경되었습니다.");
    }
}