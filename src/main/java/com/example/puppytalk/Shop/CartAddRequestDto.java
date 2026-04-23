package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequestDto {
    private Long optionId;
    private int count;
}