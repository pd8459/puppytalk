package com.example.puppytalk.Message;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageViewController {

    @GetMapping("/conversation")
    public String conversationPage(@RequestParam Long id, Model model) {
        model.addAttribute("conversationId", id);
        return "message/conversation";
    }

    @GetMapping("/inquiry")
    public String inquiryPage(@RequestParam Long id, Model model) {
        model.addAttribute("conversationId", id);
        return "inquiry/inquiry";
    }
}