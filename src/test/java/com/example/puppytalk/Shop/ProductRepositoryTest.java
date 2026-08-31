package com.example.puppytalk.Shop;


import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
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

import static org.assertj.core.api.Assertions.assertThat;

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
                    .build();

            product.setCategory(categories.get(i % 50));
            em.persist(product);
        }

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("Fetch Join 미적용 시 N+1 쿼리가 발생한다")
    void testWithoutFetchJoin() {
        Statistics statistics = getStatistics();
        statistics.clear();

        Page<Product> products = productRepository.findAll(
                PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"))
        );

        products.forEach(p -> {
            if (p.getCategory() != null) {
                p.getCategory().getName();
            }
        });

        long queryCount = statistics.getPrepareStatementCount();
        System.out.println("Fetch Join 미적용 시 실행된 쿼리 수: " + queryCount);

        assertThat(queryCount).isGreaterThan(2);
    }

    @Test
    @DisplayName("Fetch Join 최적화 테스트: N+1 없이 쿼리 2방으로 조회된다")
    void testFetchJoin() {
        Statistics statistics = getStatistics();
        statistics.clear();

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

        long queryCount = statistics.getPrepareStatementCount();
        System.out.println("Fetch Join 적용 시 실행된 쿼리 수: " + queryCount);

        assertThat(queryCount).isLessThanOrEqualTo(2);
    }

    private Statistics getStatistics() {
        Statistics statistics = em.unwrap(Session.class)
                .getSessionFactory()
                .getStatistics();
        statistics.setStatisticsEnabled(true);
        return statistics;
    }
}