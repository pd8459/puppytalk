package com.example.puppytalk.profile;

import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserDetailsImpl;
import com.example.puppytalk.image.ImageUploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PasswordChangeRequestDto requestDto) {
        try {
            profileService.changePassword(userDetails.getUser(), requestDto);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<String> updateUserNickname(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ProfileUpdateRequestDto requestDto) {

        profileService.updateNickname(userDetails.getUser(), requestDto.getNickname());
        return ResponseEntity.ok("닉네임이 성공적으로 수정되었습니다.");
    }

    @PostMapping("/image")
    public ResponseEntity<ImageUploadResponseDto> updateUserProfileImage(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("image") MultipartFile image) throws IOException {

        String imageUrl = profileService.updateProfileImage(userDetails.getUser(), image);
        return ResponseEntity.ok(new ImageUploadResponseDto(imageUrl));
    }

    @DeleteMapping
    public ResponseEntity<String> deletAccount(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AccountDeleteRequestDto requestDto) {
        try {
            profileService.deleteAccount(userDetails.getUser(), requestDto);
            return ResponseEntity.ok("회원탈퇴가 성공적으로 처리되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
