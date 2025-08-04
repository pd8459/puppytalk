package com.example.puppytalk.User;

import com.example.puppytalk.Jwt.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto requestDto) {
        String token = userService.login(requestDto);
        return ResponseEntity.ok()
                .header(JwtUtil.AUTHORIZATION_HEADER, token)
                .body("로그인에 성공했습니다.");
    }

    @GetMapping("/me")
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

}
