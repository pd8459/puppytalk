package com.example.puppytalk.Shop;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addCart(
            @RequestBody CartAddRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.addCart(userDetails.getUser(), requestDto.getProductId(), requestDto.getCount());
        return ResponseEntity.ok("장바구니에 담았습니다.");
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCartList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(cartService.getCartList(userDetails.getUser()));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.deleteCartItem(cartItemId, userDetails.getUser());
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<String> updateCartItemCount(
            @PathVariable Long cartItemId,
            @RequestParam int count,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.updateCartItemCount(cartItemId, count, userDetails.getUser());
        return ResponseEntity.ok("수량이 변경되었습니다.");
    }
}