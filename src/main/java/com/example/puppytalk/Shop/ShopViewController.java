package com.example.puppytalk.Shop;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopViewController {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductOptionRepository productOptionRepository;

    @GetMapping("/register")
    public String registerPage() {
        return "shop/register";
    }

    @GetMapping("/list")
    public String listPage() {
        return "shop/list";
    }

    @GetMapping("/detail")
    public String detailPage(@RequestParam Long id, Model model) {
        model.addAttribute("productId", id);
        return "shop/detail";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "shop/cart";
    }

    @GetMapping("/orders")
    public String orderListPage() {
        return "shop/orders";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Cart cart = cartRepository.findByUserId(userDetails.getUser().getId()).orElse(null);

        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/shop/cart";
        }

        List<CheckoutItemDto> checkoutItems = new ArrayList<>();
        int totalPrice = 0;

        for (CartItem item : cart.getItems()) {
            ProductOption option = item.getProductOption();
            Product product = option.getProduct();
            int price = product.getCurrentPrice() + option.getExtraPrice();

            CheckoutItemDto dto = new CheckoutItemDto();
            dto.setOptionId(option.getId());
            dto.setProductName(product.getName());
            dto.setOptionName(option.getName());
            dto.setThumbnailUrl(product.getThumbnailUrl());
            dto.setPrice(price);
            dto.setCount(item.getCount());

            checkoutItems.add(dto);
            totalPrice += price * item.getCount();
        }

        model.addAttribute("isDirectOrder", false);
        model.addAttribute("cartItems", checkoutItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("user", userDetails.getUser());

        return "shop/checkout";
    }

    @PostMapping("/checkout/direct")
    public String checkoutDirect(
            @RequestParam List<Long> optionIds,
            @RequestParam List<Integer> counts,
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        List<CheckoutItemDto> directItems = new ArrayList<>();
        int totalPrice = 0;

        for (int i = 0; i < optionIds.size(); i++) {
            Long optionId = optionIds.get(i);
            int count = counts.get(i);

            ProductOption option = productOptionRepository.findById(optionId)
                    .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));
            Product product = option.getProduct();
            int price = product.getCurrentPrice() + option.getExtraPrice();

            CheckoutItemDto dto = new CheckoutItemDto();
            dto.setOptionId(option.getId());
            dto.setProductName(product.getName());
            dto.setOptionName(option.getName());
            dto.setThumbnailUrl(product.getThumbnailUrl());
            dto.setPrice(price);
            dto.setCount(count);

            directItems.add(dto);
            totalPrice += price * count;
        }

        model.addAttribute("isDirectOrder", true);
        model.addAttribute("cartItems", directItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("user", userDetails.getUser());

        return "shop/checkout";
    }

    @Getter
    @Setter
    public static class CheckoutItemDto {
        private Long optionId;
        private String productName;
        private String optionName;
        private String thumbnailUrl;
        private int price;
        private int count;
    }
}
