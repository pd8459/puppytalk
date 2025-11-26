package com.example.puppytalk.Shop;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private int price;
    private int stockQuantity;
    private String description;
    private String thumbnailUrl;
    private String targetBreed;
    private DogSize recommendedSize;

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .description(description)
                .thumbnailUrl(thumbnailUrl)
                .targetBreed(targetBreed)
                .recommendedSize(recommendedSize)
                .build();
    }
}