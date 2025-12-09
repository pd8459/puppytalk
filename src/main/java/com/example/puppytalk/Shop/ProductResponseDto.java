package com.example.puppytalk.Shop;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private int originalPrice;
    private int price;
    private int discountRate;
    private String description;
    private String thumbnailUrl;
    private ProductStatus status;
    private int stockQuantity;
    private String targetBreed;
    private DogSize recommendedSize;

    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;
    private boolean isTimeDealActive;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.originalPrice = product.getOriginalPrice();
        this.description = product.getDescription();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.status = product.getStatus();
        this.stockQuantity = product.getStockQuantity();
        this.targetBreed = product.getTargetBreed();
        this.recommendedSize = product.getRecommendedSize();
        this.saleStartTime = product.getSaleStartTime();
        this.saleEndTime = product.getSaleEndTime();

        LocalDateTime now = LocalDateTime.now();

        if (product.getSaleStartTime() != null && product.getSaleEndTime() != null
                && now.isAfter(product.getSaleStartTime()) && now.isBefore(product.getSaleEndTime())) {

            this.isTimeDealActive = true;
            this.price = product.getSalePrice();
            this.discountRate = product.getDiscountRate();

        } else {
            this.isTimeDealActive = false;
            this.price = product.getOriginalPrice();
            this.discountRate = 0;
        }
    }
}