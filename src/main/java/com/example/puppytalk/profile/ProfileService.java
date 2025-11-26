package com.example.puppytalk.profile;

import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRepository;
import com.example.puppytalk.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Transactional
    public void updateNickname(User user, String newNickname) {
        User persistentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        persistentUser.updateNickname(newNickname);
    }

    @Transactional
    public void changePassword(User user, PasswordChangeRequestDto requestDto) {
        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        if(!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        User persistentUser = userRepository.findById(user.getId())
                .orElseThrow(()-> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        persistentUser.updatePassword(passwordEncoder.encode((requestDto.getNewPassword())));
    }

    @Transactional
    public String updateProfileImage(User user, MultipartFile image) throws IOException {
        User persistentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String storedFileUrl = null;
        if (image != null && !image.isEmpty()) {
            storedFileUrl = imageService.storeFile(image);
            persistentUser.updateProfileImage(storedFileUrl);
        }

        return storedFileUrl;
    }

    @Transactional
    public void  deleteAccount(User user, AccountDeleteRequestDto requestDto) {
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        User persistentUser = userRepository.findById(user.getId())
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        persistentUser.withdraw();
    }

}
