package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String targetBreed;

    @Enumerated(EnumType.STRING)
    private DogSize recommendedSize;

    @Builder
    public Product(String name, int price, int stockQuantity, String description, String thumbnailUrl, String targetBreed, DogSize recommendedSize) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.status = ProductStatus.ON_SALE;
        this.targetBreed = targetBreed;
        this.recommendedSize = recommendedSize;
    }

    // 재고 관리 로직
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
        if (this.stockQuantity == 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
        if (this.status == ProductStatus.SOLD_OUT && this.stockQuantity > 0) {
            this.status = ProductStatus.ON_SALE;
        }
    }

    public void updateProduct(String name, int price, String description, String thumbnailUrl, int stockQuantity, String targetBreed, DogSize recommendedSize) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.stockQuantity = stockQuantity;
        this.targetBreed = targetBreed;
        this.recommendedSize = recommendedSize;
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }
}