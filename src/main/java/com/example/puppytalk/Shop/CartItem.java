package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    private int count;

    public static CartItem createCartItem(Cart cart, ProductOption productOption, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductOption(productOption);
        cartItem.setCount(count);

        if (cart.getItems() != null) {
            cart.getItems().add(cartItem);
        }

        return cartItem;
    }

    public void addCount(int count) {
        this.count += count;
    }
}