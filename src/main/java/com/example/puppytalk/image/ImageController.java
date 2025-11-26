package com.example.puppytalk.image;

import com.example.puppytalk.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/images")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("이미지 파일이 비어있습니다.");
        }
        String imageUrl = imageService.storeFile(image);
        return ResponseEntity.ok(Map.of("url", imageUrl));
    }
}
