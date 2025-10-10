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
        // 쿠키의 값을 비우고 유효기간을 0으로 설정하여 삭제합니다.
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, ""); // 값을 빈 문자열로 설정
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true); // 생성 시와 동일한 HttpOnly 속성 설정
        response.addCookie(cookie);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserInfoDto> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String nickname = userDetails.getUser().getNickname();
        return ResponseEntity.ok(new UserInfoDto(nickname));
    }

    @Getter
    class UserInfoDto {
        private String nickname;
        public UserInfoDto(String nickname) {
            this.nickname = nickname;
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