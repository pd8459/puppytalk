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
    public Long createProduct(ProductRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. ID: " + dto.getCategoryId()));

        Product product = dto.toEntity(category);

        int salePrice = dto.getOriginalPrice();
        if (dto.getDiscountRate() > 0) {
            salePrice = salePrice - (salePrice * dto.getDiscountRate() / 100);
        }
        product.setSalePrice(salePrice);

        if (dto.getDetailImageUrls() != null && !dto.getDetailImageUrls().isEmpty()) {
            product.updateDetailImages(dto.getDetailImageUrls());
        }

        return productRepository.save(product).getId();
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

        if (requestDto.getDetailImageUrls() != null) {
            product.updateDetailImages(requestDto.getDetailImageUrls());
        }

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
            productPage = productRepository.searchByNameWithCategory(keyword, pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            productPage = productRepository.searchByCategoryNameWithCategory(category, pageable);
        } else {
            productPage = productRepository.findAllWithCategory(pageable);
        }

        return productPage.map(ProductResponseDto::new);
    }


}