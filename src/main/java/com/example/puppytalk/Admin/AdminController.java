package com.example.puppytalk.Admin;

import com.example.puppytalk.Shop.OrderHistoryDto;
import com.example.puppytalk.Shop.OrderStatus;
import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String adminDashboardPage() {
        return "admin/dashboard";
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<AdminDashboardDto> getStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<Page<OrderHistoryDto>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllOrders(status, pageable));
    }

    @PatchMapping("/api/orders/{orderId}/status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        adminService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("상태가 변경되었습니다.");
    }

    @GetMapping("/users")
    public String adminUsersPage() {
        return "admin/users";
    }

    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<Page<User>> getAllUsers(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @PatchMapping("/api/users/{userId}/status")
    @ResponseBody
    public ResponseEntity<String> updateUserStatus(@PathVariable Long userId, @RequestParam UserStatus status) {
        adminService.updateUserStatus(userId, status);
        return ResponseEntity.ok("회원 상태가 변경되었습니다.");
    }

    @GetMapping("/products")
    public String adminProductListPage() {
        return "admin/products";
    }

    @GetMapping("/products/edit")
    public String adminProductEditPage(@RequestParam Long id, Model model) {
        model.addAttribute("productId", id);
        return "admin/product-edit";
    }
}