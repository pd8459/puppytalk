package com.example.puppytalk.Review;

import com.example.puppytalk.Shop.Product;
import com.example.puppytalk.Shop.OrderRepository;
import com.example.puppytalk.Shop.ProductRepository;
import com.example.puppytalk.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Value("${file.upload-dir}") // 설정 파일에서 경로 가져오기
    private String uploadDir;

    @Transactional
    public Long createReview(User user, ReviewRequestDto dto, MultipartFile imageFile) throws IOException {

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        boolean hasPurchased = orderRepository.existsByUserIdAndProductId(user.getId(), product.getId());
        if (!hasPurchased) {
            throw new IllegalStateException("상품을 구매한 사용자만 후기를 작성할 수 있습니다.");
        }

        if (reviewRepository.existsByUserAndProduct(user, product)) {
            throw new IllegalStateException("이미 해당 상품에 대한 후기를 작성하셨습니다.");
        }

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            // 파일명 충돌 방지: UUID + 원래이름
            String originalFilename = imageFile.getOriginalFilename();
            String saveFileName = UUID.randomUUID() + "_" + originalFilename;

            File saveFile = new File(uploadDir, saveFileName);

            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            imageFile.transferTo(saveFile);

            imageUrl = "/images/" + saveFileName;
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .content(dto.getContent())
                .rating(dto.getRating())
                .imageUrl(imageUrl)
                .build();

        reviewRepository.save(review);
        return review.getId();
    }

    public Page<ReviewResponseDto> getReviewListByProduct(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return reviewRepository.findAllByProductOrderByCreatedAtDesc(product, pageable)
                .map(ReviewResponseDto::new);
    }

    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 후기입니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new SecurityException("본인의 후기만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }
}