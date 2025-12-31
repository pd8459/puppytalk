package com.example.puppytalk.Shop;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopViewController {

    private final CartRepository cartRepository;
    private final ProductService productService;

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
        model.addAttribute("cartItems", cart.getItems());

        int totalPrice = cart.getItems().stream()
                .mapToInt(item -> item.getProduct().getCurrentPrice() * item.getCount())
                .sum();
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("user", userDetails.getUser());

        return "shop/checkout";
    }
}