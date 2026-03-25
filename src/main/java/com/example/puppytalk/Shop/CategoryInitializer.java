package com.example.puppytalk.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("사료/간식")); // 1번
            categoryRepository.save(new Category("외출/산책")); // 2번
            categoryRepository.save(new Category("목욕/미용")); // 3번
            categoryRepository.save(new Category("의류/패션")); // 4번
            categoryRepository.save(new Category("장난감"));   // 5번
            categoryRepository.save(new Category("하우스"));   // 6번
            System.out.println("✅ 기본 카테고리 6종 세팅 완료!");
        }
    }
}