package com.example.puppytalk.ai;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.http.HttpHeaders;

@Service
public class AIService {
    private final String AI_SERVER_URL = "http://127.0.0.1:8000";

    public String classifyDogBreed(MultipartFile imageFile) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename();
            }
        });
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<DogBreedResponseDto> response = restTemplate.postForEntity(
                AI_SERVER_URL + "/predict/dog-breed",
                    requestEntity,
                    DogBreedResponseDto.class
        );
        return response.getBody().getPredicted_breed();
    }
}
