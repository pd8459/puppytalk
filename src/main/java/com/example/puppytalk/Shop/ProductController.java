package com.example.puppytalk.Shop;

import com.example.puppytalk.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final S3UploadService s3UploadService;

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(
            @RequestPart("dto") ProductRequestDto requestDto,
            @RequestPart(value = "files", required = true) List<MultipartFile> files
    ) {
        try {
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest().body("최소 한 장 이상의 이미지를 업로드해주세요.");
            }

            List<String> allUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                String url = s3UploadService.uploadFile(file);
                allUrls.add(url);
            }

            requestDto.setThumbnailUrl(allUrls.get(0));

            if (allUrls.size() > 1) {
                requestDto.setDetailImageUrls(allUrls.subList(1, allUrls.size()));
            } else {
                requestDto.setDetailImageUrls(new ArrayList<>());
            }

            Long productId = productService.createProduct(requestDto);
            return ResponseEntity.ok(productId);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("이미지 업로드 중 오류 발생");
        }
    }

    @PostMapping("/products/editor/image-upload")
    public ResponseEntity<String> uploadEditorImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageUrl = s3UploadService.uploadFile(image);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("이미지 업로드에 실패했습니다.");
        }
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