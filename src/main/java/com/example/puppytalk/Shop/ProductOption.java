package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_option")
public class ProductOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String name;

    private int extraPrice;

    @Column(nullable = false)
    private int stockQuantity;

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("해당 옵션의 재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
        this.product.checkAndAutoUpdateStatus();
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
        this.product.checkAndAutoUpdateStatus();
    }
}