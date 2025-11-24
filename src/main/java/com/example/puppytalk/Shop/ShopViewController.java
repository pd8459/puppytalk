package com.example.puppytalk.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopViewController {

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
}