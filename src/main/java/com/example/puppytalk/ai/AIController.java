package com.example.puppytalk.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/api/ai/classify-dog")
    public ResponseEntity<String> classifyDogBreed(@RequestParam("image") MultipartFile image) {
        if(image.isEmpty()) {
            return ResponseEntity.badRequest().body("이미지 파일이 비어있습니다.");
        }
        try{
            String predictedBreed = aiService.classifyDogBreed(image);
            return ResponseEntity.ok(predictedBreed);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AI서버와 통신하는데 실패했습니다.");
        }
    }

    @PostMapping("/api/ai/chat")
    public ResponseEntity<String> getChatAnswer(@RequestBody ChatRequestDto request) {
        try {
            String answer = aiService.getChatbotResponse(request.getQuestion());
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("AI 챗봇 서버와 통신하는 데 실패했습니다.");
        }
    }
}
