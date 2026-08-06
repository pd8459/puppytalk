package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (isTimeDealActive()) {
            return this.salePrice;
        }
        if (this.saleStartTime == null && this.discountRate > 0) {
            return this.salePrice;
        }
        return this.originalPrice;
    }

    private boolean isTimeDealActive() {
        if (this.saleStartTime == null || this.saleEndTime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.saleStartTime) && now.isBefore(this.saleEndTime);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addOption(ProductOption option) {
        this.options.add(option);
        option.setProduct(this);
    }

    public record OptionCommand(Long optionId, String name, int extraPrice, int stockQuantity) {}

    public void syncOptions(List<OptionCommand> commands) {
        Map<Long, ProductOption> existingById = this.options.stream()
                .filter(o -> o.getId() != null)
                .collect(Collectors.toMap(ProductOption::getId, o -> o));

        List<ProductOption> updatedList = new ArrayList<>();

        for (OptionCommand command : commands) {
            ProductOption existing = command.optionId() != null ? existingById.get(command.optionId()) : null;

            if (existing != null) {
                existing.updateInfo(command.name(), command.extraPrice(), command.stockQuantity());
                updatedList.add(existing);
            } else {
                ProductOption newOption = ProductOption.builder()
                        .name(command.name())
                        .extraPrice(command.extraPrice())
                        .stockQuantity(command.stockQuantity())
                        .build();
                addOption(newOption);
                updatedList.add(newOption);
            }
        }

        this.options.clear();
        this.options.addAll(updatedList);
        checkAndAutoUpdateStatus();
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
        List<ProductDetailImage> toRemove = this.detailImages.stream()
                .filter(img -> !imageUrls.contains(img.getImageUrl()))
                .collect(Collectors.toList());
        this.detailImages.removeAll(toRemove);

        List<String> existingUrls = this.detailImages.stream()
                .map(ProductDetailImage::getImageUrl)
                .collect(Collectors.toList());

        for (int i = 0; i < imageUrls.size(); i++) {
            String url = imageUrls.get(i);
            if (!existingUrls.contains(url)) {
                addDetailImage(url, i + 1);
            }
        }
    }
}
