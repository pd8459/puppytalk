package com.example.puppytalk.Shop;

import com.example.puppytalk.User.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
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