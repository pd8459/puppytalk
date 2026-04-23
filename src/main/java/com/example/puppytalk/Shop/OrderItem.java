package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public static OrderItem createOrderItem(ProductOption productOption, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductOption(productOption);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        productOption.removeStock(count);
        return orderItem;
    }

    public static OrderItem createNormalOrderItem(ProductOption productOption, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductOption(productOption);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        return orderItem;
    }

    public void cancel() {
        this.productOption.addStock(count);
    }

    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}