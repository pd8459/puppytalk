package com.example.puppytalk.Shop;


import com.example.puppytalk.User.User;
import com.example.puppytalk.User.UserRepository;
import com.example.puppytalk.User.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderConcurrencyTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private Long productId;
    private Long userId;

    @BeforeEach
    public void setUp() {

        Product product = new Product();
        product.setName("테스트 타임딜 상품");
        product.setOriginalPrice(1000);
        product.setSalePrice(1000);
        product.setStockQuantity(100);
        product.setDescription("테스트 설명");
        product.setStatus(com.example.puppytalk.Shop.ProductStatus.ON_SALE);
        productRepository.save(product);
        productId = product.getId();

        User user = User.builder()
                .username("testuser_concurrency")
                .password("password123")
                .nickname("테스터")
                .email("test@test.com")
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        userId = user.getId();
    }

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 100명이 주문하면 재고가 0이 되어야 한다")
    public void concurrentOrderTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    User user = userRepository.findById(userId).orElseThrow();

                    orderService.directOrder(user, productId, 1);

                    successCount.getAndIncrement();
                } catch (Exception e) {
                    failCount.getAndIncrement();
                    System.out.println("주문 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product product = productRepository.findById(productId).orElseThrow();

        System.out.println("=========================================");
        System.out.println("성공한 주문 수: " + successCount.get());
        System.out.println("실패한 주문 수: " + failCount.get());
        System.out.println("남은 재고: " + product.getStockQuantity());
        System.out.println("=========================================");

        assertThat(product.getStockQuantity()).isEqualTo(0);
    }
}