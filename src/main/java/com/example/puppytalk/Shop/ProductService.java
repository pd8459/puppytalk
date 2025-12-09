package com.example.puppytalk.Shop;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long createProduct(ProductRequestDto requestDto) {
        int salePrice = requestDto.getOriginalPrice();
        if (requestDto.getDiscountRate() > 0) {
            salePrice = salePrice - (salePrice * requestDto.getDiscountRate() / 100);
        }

        Product product = Product.builder()
                .name(requestDto.getName())
                .originalPrice(requestDto.getOriginalPrice())
                .discountRate(requestDto.getDiscountRate())
                .salePrice(salePrice)
                .description(requestDto.getDescription())
                .thumbnailUrl(requestDto.getThumbnailUrl())
                .stockQuantity(requestDto.getStockQuantity())
                .targetBreed(requestDto.getTargetBreed())
                .recommendedSize(requestDto.getRecommendedSize())
                .status(ProductStatus.ON_SALE)
                .saleStartTime(requestDto.getSaleStartTime())
                .saleEndTime(requestDto.getSaleEndTime())
                .build();

        productRepository.save(product);

        return product.getId();
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));

        int salePrice = requestDto.getOriginalPrice();
        if (requestDto.getDiscountRate() > 0) {
            salePrice = salePrice - (salePrice * requestDto.getDiscountRate() / 100);
        }

        product.updateProduct(
                requestDto.getName(),
                requestDto.getOriginalPrice(),
                requestDto.getDiscountRate(),
                salePrice,
                requestDto.getDescription(),
                requestDto.getThumbnailUrl(),
                requestDto.getStockQuantity(),
                requestDto.getTargetBreed(),
                requestDto.getRecommendedSize(),
                requestDto.getSaleStartTime(),
                requestDto.getSaleEndTime()
        );

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));
        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAllByStatusOrderByCreatedAtDesc(ProductStatus.ON_SALE).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByBreed(String breed) {
        return productRepository.findByTargetBreedAndStatus(breed, ProductStatus.ON_SALE).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsBySize(DogSize size) {
        return productRepository.findByRecommendedSizeAndStatus(size, ProductStatus.ON_SALE).stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
        product.changeStatus(ProductStatus.HIDDEN);
    }
}