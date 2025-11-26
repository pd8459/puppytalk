package com.example.puppytalk.Shop;

import com.example.puppytalk.Shop.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long cartItemId;
    private String productName;
    private int price;
    private int count;
    private String thumbnailUrl;
    private int totalPrice; // 상품가격 * 수량

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.productName = cartItem.getProduct().getName();
        this.price = cartItem.getProduct().getPrice();
        this.count = cartItem.getCount();
        this.thumbnailUrl = cartItem.getProduct().getThumbnailUrl();
        this.totalPrice = this.price * this.count;
    }
}