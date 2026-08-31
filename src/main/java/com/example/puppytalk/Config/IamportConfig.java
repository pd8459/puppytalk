package com.example.puppytalk.Config;

import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class IamportConfig {

    @Value("${iamport.key}")
    private String apiKey;

    @Value("${iamport.secret}")
    private String apiSecret;

    @PostConstruct
    public void checkKeys() {
        log.info(" [포트원 설정 확인] API KEY: [{}]", apiKey);
        log.info(" [포트원 설정 확인] API SECRET: [{}]",
                apiSecret != null && apiSecret.length() > 10 ? apiSecret.substring(0, 10) + "..." : apiSecret);
    }

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}