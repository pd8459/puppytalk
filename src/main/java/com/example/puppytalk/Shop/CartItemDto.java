package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String optionName;
    private int price;
    private int count;
    private String thumbnailUrl;
    private int totalPrice;

    public CartItemDto(CartItem cartItem) {
        ProductOption option = cartItem.getProductOption();
        Product product = option.getProduct();

        this.cartItemId = cartItem.getId();
        this.productId = product.getId();
        this.productName = product.getName();
        this.optionName = option.getName();
        this.price = product.getCurrentPrice() + option.getExtraPrice();
        this.count = cartItem.getCount();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.totalPrice = this.price * this.count;
    }
}