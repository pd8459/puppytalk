package com.example.puppytalk.Review;

import com.example.puppytalk.User.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Long> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReviewRequestDto requestDto) {

        Long reviewId = reviewService.createReview(userDetails.getUser(), requestDto);
        return ResponseEntity.ok(reviewId);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewResponseDto>> getProductReviews(
            @PathVariable Long productId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.getReviewListByProduct(productId, pageable));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return ResponseEntity.ok("후기가 삭제되었습니다.");
    }
}
