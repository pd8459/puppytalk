package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<ProductDetailImage> detailImages = new ArrayList<>();

    public void updateProduct(String name, int originalPrice, int discountRate, int salePrice,
                              String description, String thumbnailUrl,
                              String targetBreed, DogSize recommendedSize, LocalDateTime saleStartTime, LocalDateTime saleEndTime) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.salePrice = salePrice;
        this.description = description;
        if(thumbnailUrl != null) this.thumbnailUrl = thumbnailUrl;
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

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addOption(ProductOption option) {
        this.options.add(option);
        option.setProduct(this);
    }

    public void checkAndAutoUpdateStatus() {
        int totalStock = this.options.stream()
                .mapToInt(ProductOption::getStockQuantity)
                .sum();

        if (totalStock <= 0) {
            this.status = ProductStatus.SOLD_OUT;
        } else if (this.status == ProductStatus.SOLD_OUT && totalStock > 0) {
            this.status = ProductStatus.ON_SALE;
        }
    }

    public void addDetailImage(String imageUrl, int sortOrder) {
        ProductDetailImage detailImage = ProductDetailImage.builder()
                .imageUrl(imageUrl)
                .sortOrder(sortOrder)
                .product(this)
                .build();
        this.detailImages.add(detailImage);
    }

    public void updateDetailImages(List<String> imageUrls) {
        this.detailImages.clear();
        for (int i = 0; i < imageUrls.size(); i++) {
            addDetailImage(imageUrls.get(i), i + 1);
        }
    }
}