package com.example.puppytalk.Shop;

import com.example.puppytalk.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductOptionRepository productOptionRepository;

    public void addCart(User user, Long optionId, int count) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);
        if (cart == null) {
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        ProductOption option = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("옵션이 존재하지 않습니다."));

        CartItem savedCartItem = cart.getItems().stream()
                .filter(item -> item.getProductOption().getId().equals(optionId))
                .findFirst()
                .orElse(null);

        if (savedCartItem != null) {
            savedCartItem.addCount(count);
        } else {
            CartItem newCartItem = CartItem.createCartItem(cart, option, count);
            cartItemRepository.save(newCartItem);
        }
    }

    @Transactional(readOnly = true)
    public CartResponseDto getCartList(User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);

        if (cart == null) {
            return new CartResponseDto();
        }
        return new CartResponseDto(cart);
    }

    public void deleteCartItem(Long cartItemId, User user) {
        CartItem cartItem = cartItemRepository.findByIdWithCartAndUser(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템이 존재하지 않습니다."));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        cartItemRepository.delete(cartItem);
    }

    public void updateCartItemCount(Long cartItemId, int count, User user) {
        CartItem cartItem = cartItemRepository.findByIdWithCartAndUser(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템이 존재하지 않습니다."));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        cartItem.setCount(count);
    }
}