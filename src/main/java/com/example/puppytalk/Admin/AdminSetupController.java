package com.example.puppytalk.Admin;

import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRepository;
import com.example.puppytalk.User.UserRole;
import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminSetupController {

    private final UserRepository userRepository;

    @GetMapping("/setup/check")
    public String checkMyRole(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) return "로그인이 필요합니다!";

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return "<h1>현재 아이디: " + user.getUsername() + "</h1>" +
                "<h1>현재 권한(DB): " + user.getRole() + "</h1>" +
                "<h1>현재 권한(Security): " + userDetails.getAuthorities() + "</h1>";
    }

    @GetMapping("/setup/admin")
    public String forceAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) return "로그인이 필요합니다!";

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        user.promoteToAdmin();
        userRepository.save(user);

        return "<h1>🎉 관리자 승격 완료!</h1>" +
                "<p>반드시 <b>로그아웃 후 다시 로그인</b>하세요.</p>" +
                "<a href='/setup/check'>다시 확인하기</a>";
    }
}