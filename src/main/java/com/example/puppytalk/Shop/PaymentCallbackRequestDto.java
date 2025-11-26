package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCallbackRequestDto {
    private String imp_uid;
    private String merchant_uid;
    private int paid_amount;
    private String pay_method;
    private String status;
}
