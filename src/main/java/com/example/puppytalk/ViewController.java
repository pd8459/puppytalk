package com.example.puppytalk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/main")
    public  String mainPage() {
        return "main";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/write-post")
    public String writePostPage() {
        return "write-post";
    }

    @GetMapping("post-detail")
    public String postDetailPage() {
        return "post-detail";
    }

    @GetMapping("/edit-post")
    public String editPostPage() {
        return "edit-post";
    }

    @GetMapping("/classifier")
    public String classifierPage() {
        return "classifier";
    }

    @GetMapping("/chatbot")
    public String chatbotPage() {
        return "chatbot";
    }

}
