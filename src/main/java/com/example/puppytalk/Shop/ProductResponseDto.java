package com.example.puppytalk.Shop;

import lombok.Getter;

@Getter
public class ProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String description;
    private String thumbnailUrl;
    private ProductStatus status;
    private String targetBreed;
    private DogSize recommendedSize;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.description = product.getDescription();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.status = product.getStatus();
        this.targetBreed = product.getTargetBreed();
        this.recommendedSize = product.getRecommendedSize();
    }
}