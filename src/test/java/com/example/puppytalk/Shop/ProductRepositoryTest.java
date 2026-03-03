package com.example.puppytalk.Shop;


import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private EntityManager em;

    @BeforeEach
    void setUp() {
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Category category = new Category("카테고리" + i);
            em.persist(category);
            categories.add(category);
        }

        for (int i = 1; i <= 100; i++) {
            Product product = Product.builder()
                    .name("테스트 상품 " + i)
                    .stockQuantity(10)
                    .build();

            product.setCategory(categories.get(i % 50));
            em.persist(product);
        }

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("Fetch Join 최적화 테스트: N+1 쿼리가 1방으로 줄어드는 마법!")
    void testFetchJoin() {
        System.out.println("====== Fetch Join 조회 시작 ======");

        Page<Product> products = productRepository.findAllWithCategory(
                PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"))
        );

        products.forEach(p -> {
            if (p.getCategory() != null) {
                p.getCategory().getName();
            }
        });

        System.out.println("====== Fetch Join 조회 종료 ======");
    }
}