package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;
    private static final int IAMPORT_PAYMENT_LOOKUP_RETRY_COUNT = 3;

    @Transactional(noRollbackFor = PaymentVerificationException.class)
    public void verifyAndSavePayment(PaymentCallbackRequestDto requestDto, User user) {
        validatePaymentRequest(requestDto);

        paymentRepository.findByImpUid(requestDto.getImp_uid()).ifPresent(payment -> {
            throw new IllegalStateException("이미 처리된 결제입니다.");
        });
        paymentRepository.findByMerchantUid(requestDto.getMerchant_uid()).ifPresent(payment -> {
            throw new IllegalStateException("이미 처리된 주문 결제번호입니다.");
        });

        Long orderId = extractOrderId(requestDto.getMerchant_uid());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인의 주문만 결제할 수 있습니다.");
        }

        IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse;
        try {
            iamportResponse = findPaymentWithRetry(requestDto.getImp_uid());
        } catch (IamportResponseException | IOException e) {
            order.cancel();
            throw new PaymentVerificationException(
                    "결제 정보 조회에 실패했습니다: " + e.getMessage()
                            + " imp_uid와 PortOne 가맹점 식별코드/REST API 키가 같은 계정인지 확인해주세요."
            );
        }

        com.siot.IamportRestClient.response.Payment verifiedPayment = iamportResponse.getResponse();
        if (verifiedPayment == null) {
            order.cancel();
            throw new PaymentVerificationException("결제 정보를 찾을 수 없습니다.");
        }

        if (!requestDto.getMerchant_uid().equals(verifiedPayment.getMerchantUid())) {
            cancelIamportPayment(requestDto.getImp_uid());
            order.cancel();
            throw new PaymentVerificationException("주문 결제번호가 일치하지 않습니다.");
        }

        int verifiedAmount = verifiedPayment.getAmount().intValue();
        int expectedAmount = order.getTotalPrice();

        if (verifiedAmount != expectedAmount) {
            cancelIamportPayment(requestDto.getImp_uid());
            order.cancel();
            throw new PaymentVerificationException(
                    "결제 금액이 일치하지 않습니다. 검증된 금액: " + verifiedAmount + ", 주문 금액: " + expectedAmount
            );
        }

        if (!"paid".equals(verifiedPayment.getStatus())) {
            order.cancel();
            throw new PaymentVerificationException("결제가 완료되지 않았습니다. 상태: " + verifiedPayment.getStatus());
        }

        if (order.getStatus() == OrderStatus.PENDING) {
            order.updateStatus(OrderStatus.ORDER);
        }

        Payment payment = Payment.builder()
                .order(order)
                .impUid(requestDto.getImp_uid())
                .merchantUid(requestDto.getMerchant_uid())
                .amount(verifiedAmount)
                .payMethod(requestDto.getPay_method())
                .status("PAID")
                .build();

        paymentRepository.save(payment);
    }

    private void validatePaymentRequest(PaymentCallbackRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("결제 요청 정보가 없습니다.");
        }
        if (!StringUtils.hasText(requestDto.getImp_uid())) {
            throw new IllegalArgumentException("결제 고유번호(impUid)가 없습니다.");
        }
        if (!StringUtils.hasText(requestDto.getMerchant_uid())) {
            throw new IllegalArgumentException("주문 결제번호(merchantUid)가 없습니다.");
        }
    }

    private Long extractOrderId(String merchantUid) {
        String[] parts = merchantUid.split("-", 3);
        if (parts.length < 3 || !"ORD".equals(parts[0])) {
            throw new IllegalArgumentException("올바르지 않은 주문 결제번호입니다.");
        }
        try {
            return Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("주문 번호를 확인할 수 없습니다.");
        }
    }

    private IamportResponse<com.siot.IamportRestClient.response.Payment> findPaymentWithRetry(String impUid)
            throws IamportResponseException, IOException {
        IamportResponse<com.siot.IamportRestClient.response.Payment> response = null;
        IamportResponseException lastIamportException = null;
        IOException lastIOException = null;

        for (int attempt = 1; attempt <= IAMPORT_PAYMENT_LOOKUP_RETRY_COUNT; attempt++) {
            try {
                response = iamportClient.paymentByImpUid(impUid);
                if (response.getResponse() != null) {
                    return response;
                }
            } catch (IamportResponseException e) {
                lastIamportException = e;
            } catch (IOException e) {
                lastIOException = e;
            }

            sleepBeforeRetry(attempt);
        }

        if (lastIamportException != null) {
            throw lastIamportException;
        }
        if (lastIOException != null) {
            throw lastIOException;
        }
        return response;
    }

    private void sleepBeforeRetry(int attempt) {
        if (attempt >= IAMPORT_PAYMENT_LOOKUP_RETRY_COUNT) {
            return;
        }
        try {
            Thread.sleep(500L * attempt);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void cancelIamportPayment(String impUid) {
        try {
            CancelData cancelData = new CancelData(impUid, true);
            IamportResponse<com.siot.IamportRestClient.response.Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);

            if (response.getCode() != 0) {
                throw new RuntimeException("결제 취소 실패: " + response.getMessage());
            }

            log.info("결제 취소 성공! imp_uid: {}", impUid);
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException("결제 취소 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public void cancelPayment(String impUid) throws IamportResponseException, IOException {
        if (!StringUtils.hasText(impUid)) {
            throw new IllegalArgumentException("결제 고유번호(impUid)가 없습니다.");
        }

        cancelIamportPayment(impUid);

        Payment payment = paymentRepository.findByImpUidWithOrder(impUid)
                .orElseThrow(() -> new RuntimeException("해당 결제 내역이 존재하지 않습니다."));

        payment.updateStatus("CANCEL");

        if (payment.getOrder() != null) {
            payment.getOrder().updateStatus(OrderStatus.CANCEL);
        }
    }

    public static class PaymentVerificationException extends RuntimeException {
        public PaymentVerificationException(String message) {
            super(message);
        }
    }
}
