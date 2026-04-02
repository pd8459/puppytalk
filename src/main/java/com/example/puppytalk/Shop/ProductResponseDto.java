package com.example.puppytalk.Shop;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> detailImageUrls;
    private ProductStatus status;
    private int stockQuantity;
    private String targetBreed;
    private DogSize recommendedSize;
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;
    private boolean isTimeDealActive;
    private String categoryName;

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
        if (product.getCategory() != null) {
            this.categoryName = product.getCategory().getName();
        }

        if (product.getDetailImages() != null && !product.getDetailImages().isEmpty()) {
            this.detailImageUrls = product.getDetailImages().stream()
                    .map(ProductDetailImage::getImageUrl)
                    .collect(Collectors.toList());
        }

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