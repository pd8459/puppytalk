package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public static OrderItem createOrderItem(Product product, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.product = product;
        orderItem.orderPrice = product.getPrice();
        orderItem.count = count;
        product.removeStock(count);

        return orderItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        getProduct().addStock(count);
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}