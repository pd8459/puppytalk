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
        return ResponseEntity.ok(productService.createProduct(requestDto));
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        System.out.println("👉 [CCTV] 프론트에서 받은 검색어: [" + keyword + "] / 카테고리: [" + category + "]");

        return ResponseEntity.ok(productService.getProducts(keyword, category, pageable));
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