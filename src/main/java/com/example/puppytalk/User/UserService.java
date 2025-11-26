package com.example.puppytalk.User;

import com.example.puppytalk.Jwt.JwtUtil;
import com.example.puppytalk.Post.Post;
import com.example.puppytalk.Post.PostRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Builder
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;

    @Transactional
    public User signup(UserSignupRequestDto requestDto){
        if(userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User newUser = User.builder()
                .username(requestDto.getUsername())
                .nickname(requestDto.getNickname())
                .password(encodedPassword)
                .email(requestDto.getEmail())
                .build();
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public String login(UserLoginRequestDto requestDto) {

        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (user.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getUsername());
    }

    @Transactional(readOnly = true)
    public PublicProfileResponseDto getPublicProfile(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Page<Post> postPage = postRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return new PublicProfileResponseDto(user, postPage);
    }
}

