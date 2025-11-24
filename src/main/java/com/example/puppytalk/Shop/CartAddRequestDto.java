package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequestDto {
    private Long productId;
    private int count;
}