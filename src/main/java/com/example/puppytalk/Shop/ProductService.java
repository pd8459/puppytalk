package com.example.puppytalk.Shop;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long createProduct(ProductRequestDto requestDto) {
        return productRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + productId));

        product.updateProduct(
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getDescription(),
                requestDto.getThumbnailUrl()
        );
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
}