package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DirectOrderDto {

    private List<OrderItemDto> orderItems;

    private String recipient;
    private String phoneNumber;
    private String address;

    @Getter
    @Setter
    public static class OrderItemDto {
        private Long optionId;
        private int count;
    }
}