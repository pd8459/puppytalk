package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CartResponseDto {
    private Long cartId;
    private List<CartItemDto> cartItems;
    private int totalOrderPrice;

    public CartResponseDto(Cart cart) {
        this.cartId = cart.getId();
        this.cartItems = cart.getItems().stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());

        this.totalOrderPrice = this.cartItems.stream()
                .mapToInt(CartItemDto::getTotalPrice)
                .sum();
    }
}