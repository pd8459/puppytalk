package com.example.puppytalk.Shop;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int originalPrice;

    private int salePrice;

    private int discountRate;

    @Column(nullable = false)
    private int stockQuantity;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String targetBreed;

    @Enumerated(EnumType.STRING)
    private DogSize recommendedSize;

    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;

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

    public void updateProduct(String name, int originalPrice, int discountRate, int salePrice,
                              String description, String thumbnailUrl, int stockQuantity,
                              String targetBreed, DogSize recommendedSize,LocalDateTime saleStartTime, LocalDateTime saleEndTime) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.salePrice = salePrice;
        this.description = description;
        if(thumbnailUrl != null) this.thumbnailUrl = thumbnailUrl;
        this.stockQuantity = stockQuantity;
        this.targetBreed = targetBreed;
        this.recommendedSize = recommendedSize;
        this.saleStartTime = saleStartTime;
        this.saleEndTime = saleEndTime;
    }

    public void changeStatus(ProductStatus status) {
        this.status = status;
    }

    public void applyDiscount(int discountRate) {
        this.discountRate = discountRate;
        this.salePrice = this.originalPrice - (this.originalPrice * discountRate / 100);
    }

    public int getCurrentPrice() {
        LocalDateTime now = LocalDateTime.now();
        if (this.saleStartTime != null && this.saleEndTime != null) {
            if (now.isAfter(this.saleStartTime) && now.isBefore(this.saleEndTime)) {
                return this.salePrice;
            }
        }
        else if (this.discountRate > 0) {
            return this.salePrice;
        }
        return this.originalPrice;
    }
}