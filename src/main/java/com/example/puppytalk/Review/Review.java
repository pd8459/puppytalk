package com.example.puppytalk.Review;

import com.example.puppytalk.Shop.BaseTimeEntity;
import com.example.puppytalk.Shop.Product;
import com.example.puppytalk.User.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="review")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int rating;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Review(String content, int rating, String imageUrl, Product product, User user) {
        this.content = content;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.product = product;
        this.user = user;
    }

    public void updateReview(String content, int rating, String imageUrl) {
        this.content = content;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }
}
