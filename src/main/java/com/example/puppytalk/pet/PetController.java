package com.example.puppytalk.pet;

import com.example.puppytalk.User.UserDetailsImpl;
import com.example.puppytalk.image.Image;
import com.example.puppytalk.image.ImageUploadResponseDto;
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

    @PostMapping
    public ResponseEntity<PetResponseDto> createPet(@RequestBody PetRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PetResponseDto responseDto = petService.createPet(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<PetResponseDto>> getPets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PetResponseDto> pets = petService.getPets(userDetails.getUser());
        return ResponseEntity.ok(pets);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetResponseDto> updatePet(@PathVariable Long petId, @RequestBody PetRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PetResponseDto updatedPet = petService.updatePet(petId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<String> deletePet(@PathVariable Long petId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        petService.deletePet(petId, userDetails.getUser());
        return ResponseEntity.ok("반려견 프로필이 삭제되었습니다.");
    }

    @PostMapping("/{petId}/image")
    public ResponseEntity<ImageUploadResponseDto> uploadPetProfileImage(
            @PathVariable Long petId,
            @RequestParam("image")MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String imageUrl = petService.updatePetProfileImage(petId, image, userDetails.getUser());
        return ResponseEntity.ok(new ImageUploadResponseDto(imageUrl));
    }
}
