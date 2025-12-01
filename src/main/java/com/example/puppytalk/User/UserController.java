package com.example.puppytalk.User;

import com.example.puppytalk.Jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/users/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto requestDto, HttpServletResponse response) {
        String token = userService.login(requestDto);
        jwtUtil.addJwtToCookie(token, response);
        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInfoDto> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new UserInfoDto(userDetails.getUser()));
    }

    @Getter
    static class UserInfoDto { // DTO
        private String username;
        private String nickname;
        private String profileImageUrl;
        private UserRole role;

        public UserInfoDto(User user) {
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.profileImageUrl = user.getProfileImageUrl();
            this.role = user.getRole();
        }
    }

    @GetMapping("/users/public-profile/{username}")
    public ResponseEntity<PublicProfileResponseDto> getPublicProfile(
            @PathVariable String username,
            @PageableDefault(size=10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PublicProfileResponseDto profileDto = userService.getPublicProfile(username, pageable);
        return ResponseEntity.ok(profileDto);
    }
}