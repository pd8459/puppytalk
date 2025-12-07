package com.example.puppytalk.Jwt;

import com.example.puppytalk.User.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException; // 👈 추가됨
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String username, UserRole role) {
        Date date = new Date();
        if (role == null) {
            role = UserRole.USER;
        }

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public void addJwtToCookie(String token, HttpServletResponse response) {
        try {
            token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String decodeToken(String tokenValue) {
        if (tokenValue == null) return null;
        try {
            return URLDecoder.decode(tokenValue, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return tokenValue;
        }
    }

    public String substringToken(String tokenValue) {
        if (!StringUtils.hasText(tokenValue)) {
            return null;
        }
        tokenValue = decodeToken(tokenValue);
        tokenValue = tokenValue.trim();

        if (tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7).trim();
        }
        return tokenValue;
    }

    public boolean validateToken(String token) {
        try {
            token = decodeToken(token);
            if (token != null && token.startsWith(BEARER_PREFIX)) {
                token = token.substring(7).trim();
            }

            if (!StringUtils.hasText(token)) return false;

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty");
        } catch (DecodingException e) {
            // ⬇️ [핵심] 이 에러가 잡히지 않아서 서버가 멈췄던 것입니다.
            // % 문자 등이 섞여있어서 디코딩 못할 때 발생하는 에러를 여기서 잡습니다.
            log.error("JWT decoding failed: {}", e.getMessage());
        } catch (Exception e) {
            // 그 외 모든 에러도 안전하게 처리
            log.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        token = decodeToken(token);
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(7).trim();
        }
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    return decodeToken(cookie.getValue());
                }
            }
        }
        return null;
    }
}