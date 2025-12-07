package com.example.puppytalk.User;

import com.example.puppytalk.Jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public void naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getToken(code, state);
        NaverUserInfoDto naverUserInfo = getNaverUserInfo(accessToken);
        User naverUser = registerNaverUserIfNeeded(naverUserInfo);
        String token = jwtUtil.createToken(naverUser.getUsername(), naverUser.getRole());
        jwtUtil.addJwtToCookie(token, response);
    }

    private String getToken(String code, String state) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://nid.naver.com")
                .path("/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private NaverUserInfoDto getNaverUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/nid/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).headers(headers).build();
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        JsonNode responseNode = jsonNode.get("response");

        String id = responseNode.get("id").asText();
        String nickname = responseNode.get("nickname").asText();
        String email = responseNode.get("email").asText();
        String profileImage = responseNode.has("profile_image") ? responseNode.get("profile_image").asText() : null;

        return new NaverUserInfoDto(id, nickname, email, profileImage);
    }

    private User registerNaverUserIfNeeded(NaverUserInfoDto naverUserInfo) {
        String naverId = naverUserInfo.getId();
        User naverUser = userRepository.findByNaverId(naverId).orElse(null);

        if (naverUser == null) {
            String email = naverUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(email).orElse(null);

            if (sameEmailUser != null) {
                naverUser = sameEmailUser;
                naverUser.naverIdUpdate(naverId);
            } else {
                String username = "naver_" + naverId.substring(0, 8);
                String password = passwordEncoder.encode(UUID.randomUUID().toString());

                String uniqueNickname = naverUserInfo.getNickname() + "_" + UUID.randomUUID().toString().substring(0, 4);

                naverUser = User.builder()
                        .username(username)
                        .password(password)
                        .nickname(uniqueNickname)
                        .email(email)
                        .profileImageUrl(naverUserInfo.getProfileImage())
                        .role(UserRole.USER)
                        .status(UserStatus.ACTIVE)
                        .naverId(naverId)
                        .build();
            }
            userRepository.save(naverUser);
        }
        return naverUser;
    }
}