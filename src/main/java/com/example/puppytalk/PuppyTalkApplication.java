package com.example.puppytalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PuppyTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuppyTalkApplication.class, args);
    }

}
