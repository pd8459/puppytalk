package com.example.puppytalk.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PuppyTalk API")
                        .description(
                                "반려견 커뮤니티 & 커머스 플랫폼 API 문서\n\n" +
                                        "인증은 HttpOnly 쿠키 기반으로 동작합니다. " +
                                        "'/api/users/login'으로 먼저 로그인하면 브라우저가 쿠키를 자동으로 저장하고, " +
                                        "이후 요청에 자동으로 실어 보냅니다. 별도로 토큰을 입력할 필요는 없습니다.")
                        .version("1.0.0"));
    }
}