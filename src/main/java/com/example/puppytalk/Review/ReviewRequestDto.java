package com.example.puppytalk.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @NotBlank(message = "후기 내용은 필수입니다.")
    private String content;

    @Min(value = 1, message = "별점은 최소 1점입니다.")
    @Max(value = 5, message = "별점은 최대 5점입니다.")
    private int rating;

    private String imageUrl;
}
