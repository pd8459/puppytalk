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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private Long productId;
    private Long optionId;
    private Long userId;

    @BeforeEach
    public void setUp() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        Product product = new Product();
        product.setName("테스트 타임딜 상품 " + suffix);
        product.setOriginalPrice(1000);
        product.setSalePrice(1000);
        product.setSaleStartTime(LocalDateTime.now().minusMinutes(1));
        product.setSaleEndTime(LocalDateTime.now().plusMinutes(10));
        product.setDescription("테스트 설명");
        product.setStatus(com.example.puppytalk.Shop.ProductStatus.ON_SALE);
        product.addOption(ProductOption.builder()
                .name("기본 옵션")
                .stockQuantity(100)
                .extraPrice(0)
                .build());
        productRepository.save(product);
        productId = product.getId();
        optionId = product.getOptions().get(0).getId();

        User user = User.builder()
                .username("testuser_concurrency_" + suffix)
                .password("password123")
                .nickname("테스터_" + suffix)
                .email("test_" + suffix + "@test.com")
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        userId = user.getId();
    }

    @AfterEach
    public void tearDown() {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> {
                List<Order> orders = orderRepository.findAllByUserOrderByOrderDateDesc(user);
                orderRepository.deleteAll(orders);
            });
        }
        if (productId != null) {
            productRepository.deleteById(productId);
        }
        if (userId != null) {
            userRepository.deleteById(userId);
        }
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

                    orderService.directOrder(user, createDirectOrderRequest());

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
        executorService.shutdown();

        ProductOption option = productOptionRepository.findById(optionId).orElseThrow();

        System.out.println("=========================================");
        System.out.println("성공한 주문 수: " + successCount.get());
        System.out.println("실패한 주문 수: " + failCount.get());
        System.out.println("남은 재고: " + option.getStockQuantity());
        System.out.println("=========================================");

        assertThat(option.getStockQuantity()).isEqualTo(0);
        assertThat(successCount.get()).isEqualTo(threadCount);
    }

    private DirectOrderDto createDirectOrderRequest() {
        DirectOrderDto request = new DirectOrderDto();
        DirectOrderDto.OrderItemDto item = new DirectOrderDto.OrderItemDto();
        item.setOptionId(optionId);
        item.setCount(1);
        request.setOrderItems(List.of(item));
        return request;
    }
}
