package com.example.puppytalk.Shop;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long createProduct(ProductRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

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
                .category(category)
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
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto::new);

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

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(String keyword, Pageable pageable) {
        Page<Product> productPage;

        if (keyword == null || keyword.trim().isEmpty()) {
            productPage = productRepository.findAllWithCategory(pageable);
        } else {
            productPage = productRepository.searchByNameWithCategory(keyword, pageable);
        }
        return productPage.map(ProductResponseDto::new);
    }


    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(String keyword, String category, Pageable pageable) {
        Page<Product> productPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // ✨ 검색어 최적화 쿼리 사용!
            productPage = productRepository.searchByNameWithCategory(keyword, pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            // ✨ 카테고리 최적화 쿼리 사용!
            productPage = productRepository.searchByCategoryNameWithCategory(category, pageable);
        } else {
            // ✨ 전체 조회 최적화 쿼리 사용!
            productPage = productRepository.findAllWithCategory(pageable);
        }

        return productPage.map(ProductResponseDto::new);
    }

}