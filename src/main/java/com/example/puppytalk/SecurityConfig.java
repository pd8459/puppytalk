package com.example.puppytalk;

import com.example.puppytalk.Jwt.JwtAuthenticationFilter;
import com.example.puppytalk.Jwt.JwtUtil;
import com.example.puppytalk.User.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.headers(headers ->
                headers.contentSecurityPolicy(csp ->
                        csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline' *.kakao.com t1.daumcdn.net cdn.jsdelivr.net uicdn.toast.com https://cdn.iamport.kr; " +
                                        "img-src 'self' data: https: *.daumcdn.net; " +
                                        "connect-src 'self' https: ws: wss:; " +
                                        "frame-src 'self' https:; " +
                                        "style-src 'self' 'unsafe-inline' cdn.jsdelivr.net uicdn.toast.com https://cdnjs.cloudflare.com; " +
                                        "font-src 'self' cdn.jsdelivr.net https://cdnjs.cloudflare.com;"
                        )
                )
        );

        String[] PUBLIC_URLS = {
                "/", "/login", "/signup", "/main", "/post-detail", "/playgrounds", "/hospitals",
                "/public-profile/**", "/api/users/signup", "/api/users/login", "/api/logout",
                "/api/ai/**", "/images/**", "/css/**", "/js/**",
                "/classifier", "/chatbot", "/api/playgrounds/**", "/api/hospitals/**",
                "/ws-stomp/**", "/shop/**", "/api/shop/**","/admin/**", "/admin/api/**",
        };

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/admin/**", "/admin/api/**").hasAuthority("ADMIN")
                .requestMatchers("/shop/admin/**", "/shop/register").hasAuthority("ADMIN")
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:63342", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:3000"));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}