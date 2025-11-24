package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderHistoryDto {
    private Long orderId;
    private String orderDate;
    private OrderStatus status;
    private int totalPrice;
    private List<OrderItemDto> orderItems;

    public OrderHistoryDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class OrderItemDto {
        private String productName;
        private int count;
        private int orderPrice;
        private String thumbnailUrl;

        public OrderItemDto(OrderItem orderItem) {
            this.productName = orderItem.getProduct().getName();
            this.count = orderItem.getCount();
            this.orderPrice = orderItem.getOrderPrice();
            this.thumbnailUrl = orderItem.getProduct().getThumbnailUrl();
        }
    }
}