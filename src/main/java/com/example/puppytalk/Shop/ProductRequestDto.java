package com.example.puppytalk.Shop;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime saleStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime saleEndTime;

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .originalPrice(originalPrice)
                .stockQuantity(stockQuantity)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .targetBreed(targetBreed)
                .recommendedSize(recommendedSize)
                .build();
    }
}