package com.example.puppytalk.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public void verifyAndSavePayment(PaymentCallbackRequestDto requestDto) {
        String[] parts = requestDto.getMerchant_uid().split("-");
        Long orderId = Long.parseLong(parts[1]);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

        if(order.getTotalPrice() != requestDto.getPaid_amount()) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        Payment payment = Payment.builder()
                .order(order)
                .impUid(requestDto.getImp_uid())
                .merchantUid(requestDto.getMerchant_uid())
                .amount(requestDto.getPaid_amount())
                .payMethod(requestDto.getPay_method())
                .status("PAID")
                .build();

        paymentRepository.save(payment);

    }
}
