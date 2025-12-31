package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DirectOrderDto {
    private Long productId;
    private int count;
}
