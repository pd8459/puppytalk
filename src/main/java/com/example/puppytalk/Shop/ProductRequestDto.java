package com.example.puppytalk.Shop;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private int originalPrice;
    private int discountRate;
    private int stockQuantity;
    private String description;
    private String thumbnailUrl;
    private String targetBreed;
    private DogSize recommendedSize;
    private Long categoryId;
    private List<String> detailImageUrls;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime saleStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime saleEndTime;

    public Product toEntity(Category category) {
        Product product = Product.builder()
                .name(name)
                .originalPrice(originalPrice)
                .discountRate(discountRate)
                .stockQuantity(stockQuantity)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .targetBreed(targetBreed)
                .recommendedSize(recommendedSize)
                .saleStartTime(saleStartTime)
                .saleEndTime(saleEndTime)
                .category(category)
                .status(ProductStatus.ON_SALE)
                .build();

        if (detailImageUrls != null && !detailImageUrls.isEmpty()) {
            for (int i = 0; i < detailImageUrls.size(); i++) {
                product.addDetailImage(detailImageUrls.get(i), i + 1);
            }
        }
        product.applyDiscount(discountRate);

        return product;
    }
}