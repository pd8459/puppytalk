package com.example.puppytalk.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Long> createProduct(@RequestBody ProductRequestDto requestDto) {
        Long productId = productService.createProduct(requestDto);
        return ResponseEntity.ok(productId);
    }

    // ✨ 요놈 하나로 합쳤습니다! (기존 getAllProducts 삭제)
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(required = false) String keyword, // 검색어 받기
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        System.out.println("🚨 프론트에서 넘어온 검색어: " + keyword);
        Page<ProductResponseDto> products = productService.getProducts(keyword, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable Long productId, @RequestBody ProductRequestDto requestDto) {
        productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok("상품이 수정되었습니다.");
    }

    @GetMapping("/recommend/breed")
    public ResponseEntity<List<ProductResponseDto>> getRecommendedProductsByBreed(@RequestParam String breed) {
        return ResponseEntity.ok(productService.getProductsByBreed(breed));
    }

    @GetMapping("/recommend/size")
    public ResponseEntity<List<ProductResponseDto>> getRecommendedProductsBySize(@RequestParam DogSize size) {
        return ResponseEntity.ok(productService.getProductsBySize(size));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("상품이 삭제(숨김) 처리되었습니다.");
    }
}