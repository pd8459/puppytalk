package com.example.puppytalk.Shop;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    public void verifyAndSavePayment(PaymentCallbackRequestDto requestDto) {
        String[] parts = requestDto.getMerchant_uid().split("-");
        Long orderId = Long.parseLong(parts[1]);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.ORDER);
            orderRepository.save(order);
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

    public void cancelPayment(String impUid) throws IamportResponseException, IOException {

        if (impUid == null || impUid.isEmpty()) {
            throw new IllegalArgumentException("결제 고유번호(impUid)가 없습니다.");
        }

        CancelData cancelData = new CancelData(impUid, true);

        IamportResponse<com.siot.IamportRestClient.response.Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);

        if (response.getCode() != 0) {
            throw new RuntimeException("결제 취소 실패: " + response.getMessage());
        }

        log.info("결제 취소 성공! imp_uid: {}", impUid);

        Payment payment = paymentRepository.findByImpUid(impUid)
                .orElse(null);
        if (payment != null) {
            payment.setStatus("CANCEL");
        }
    }
}