package com.example.puppytalk.User;

import com.example.puppytalk.Jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    public void googleLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getToken(code);
        GoogleUserInfoDto googleUserInfo = getGoogleUserInfo(accessToken);
        User googleUser = registerGoogleUserIfNeeded(googleUserInfo);
        String token = jwtUtil.createToken(googleUser.getUsername(), googleUser.getRole());
        jwtUtil.addJwtToCookie(token, response);
    }

    private String getToken(String code) throws JsonProcessingException {
        String uri = "https://oauth2.googleapis.com/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", "http://localhost:8080/api/user/google/callback");
        body.add("grant_type", "authorization_code");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri).headers(headers).body(body);
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        return new ObjectMapper().readTree(response.getBody()).get("access_token").asText();
    }

    private GoogleUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        String uri = "https://www.googleapis.com/oauth2/v2/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).headers(headers).build();
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        String id = jsonNode.get("id").asText();
        String email = jsonNode.get("email").asText();
        String name = jsonNode.get("name").asText();
        String picture = jsonNode.has("picture") ? jsonNode.get("picture").asText() : null;

        return new GoogleUserInfoDto(id, email, name, picture);
    }

    private User registerGoogleUserIfNeeded(GoogleUserInfoDto googleUserInfo) {
        String googleId = googleUserInfo.getId();
        User googleUser = userRepository.findByGoogleId(googleId).orElse(null);

        if (googleUser == null) {
            String email = googleUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(email).orElse(null);

            if (sameEmailUser != null) {
                googleUser = sameEmailUser;
                googleUser.googleIdUpdate(googleId);
            } else {
                String username = "google_" + googleId.substring(0, 8);
                String password = passwordEncoder.encode(UUID.randomUUID().toString());

                String uniqueNickname = googleUserInfo.getName() + "_" + UUID.randomUUID().toString().substring(0, 4);

                googleUser = User.builder()
                        .username(username)
                        .password(password)
                        .nickname(uniqueNickname)
                        .email(email)
                        .profileImageUrl(googleUserInfo.getPicture())
                        .role(UserRole.USER)
                        .status(UserStatus.ACTIVE)
                        .googleId(googleId)
                        .build();
            }
            userRepository.save(googleUser);
        }
        return googleUser;
    }
}