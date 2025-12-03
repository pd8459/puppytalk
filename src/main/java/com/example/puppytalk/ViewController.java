package com.example.puppytalk;

import com.example.puppytalk.User.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping({"/", "/main"})
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

    @GetMapping("/post-detail")
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

    @GetMapping("/mypage")
    public String mypagePage() { return "mypage";}

    @GetMapping("/profile-edit")
    public String profileEditPage() { return "profile-edit";}

    @GetMapping("/messages")
    public String messagesPage() {
        return "messages";
    }

    @GetMapping("/notifications")
    public String notificationsPage() {
        return "notifications";
    }

    @GetMapping("/public-profile/{username}")
    public String publicProfilePage() {
        return "public-profile";
    }

    @GetMapping("/playgrounds")
    public String playgroundsPage() { return "playgrounds";}

    @GetMapping("/hospitals")
    public String hospitalsPage() {
        return "hospitals";
    }

}
