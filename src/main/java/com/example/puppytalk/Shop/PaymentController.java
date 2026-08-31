package com.example.puppytalk.Shop;

import com.example.puppytalk.Shop.PaymentCallbackRequestDto;
import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api/shop/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody PaymentCallbackRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("👉 프론트에서 전달받은 imp_uid: {}", requestDto.getImp_uid());
        log.info("👉 프론트에서 전달받은 merchant_uid: {}", requestDto.getMerchant_uid());

        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }
            paymentService.verifyAndSavePayment(requestDto, userDetails.getUser());
            return ResponseEntity.ok("결제 검증 및 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
