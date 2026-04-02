package com.example.puppytalk.pet;

import com.example.puppytalk.S3UploadService;
import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private final S3UploadService s3UploadService;

    @PostMapping
    public ResponseEntity<PetResponseDto> createPet(
            @RequestPart("dto") PetRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        if (image != null && !image.isEmpty()) {
            String imageUrl = s3UploadService.uploadFile(image);
            requestDto.setProfileImageUrl(imageUrl);
        }

        PetResponseDto responseDto = petService.createPet(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getPets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PetResponseDto> pets = petService.getPets(userDetails.getUser());
        return ResponseEntity.ok(pets);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable Long petId,
            @RequestPart("dto") PetRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        if (image != null && !image.isEmpty()) {
            String imageUrl = s3UploadService.uploadFile(image);
            requestDto.setProfileImageUrl(imageUrl);
        }

        PetResponseDto updatedPet = petService.updatePet(petId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<String> deletePet(
            @PathVariable Long petId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        petService.deletePet(petId, userDetails.getUser());
        return ResponseEntity.ok("반려견 프로필이 삭제되었습니다.");
    }
}