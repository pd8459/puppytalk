package com.example.puppytalk.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private String nickname;
    private String productName;
    private String content;
    private int rating;
    private String imageUrl;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.nickname = review.getUser().getNickname();
        this.productName = review.getProduct().getName();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.imageUrl = review.getImageUrl();
        this.createdAt = review.getCreatedAt();
    }
}
