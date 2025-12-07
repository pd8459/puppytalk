package com.example.puppytalk.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${naver.client.id}")
    private String naverClientId;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("kakaoClientId", kakaoClientId);
        model.addAttribute("googleClientId", googleClientId);
        model.addAttribute("naverClientId", naverClientId);
        model.addAttribute("naverState", "test_state_string");
        return "login";
    }
}