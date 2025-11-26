package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int count;

    @Builder
    public CartItem(Cart cart, Product product, int count) {
        this.cart = cart;
        this.product = product;
        this.count = count;
    }

    public static CartItem createCartItem(Cart cart, Product product, int count) {
        return CartItem.builder()
                .cart(cart)
                .product(product)
                .count(count)
                .build();
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}