package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.service.ProductService;
import com.apollo.scentraapi.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    @GetMapping("/{id}")
    @Operation(summary="상품 조회")
    public ApiResponse<ProductResponse.ProductDto> getProduct(@PathVariable Long id) {
        ProductResponse.ProductDto response = productService.getProduct(id);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping
    @Operation(summary="상품 목록 조회")
    public ApiResponse<List<ProductResponse.ProductListDto>> getAllProducts() {
        List<ProductResponse.ProductListDto> response = productService.getAllProducts();
        return ApiResponse.onSuccess(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary="상품 업로드", description = "**추천 성별**은 MALE or FEMALE or BOTH로 입력해주세요. <br> **카테고리명**은 한국어로 입력해주세요.")
    public ApiResponse<ProductResponse.ProductDto> uploadProduct(@RequestPart MultipartFile productImage,
                                                                 @RequestPart MultipartFile detailImage,
                                                                 @RequestPart("request") @Valid ProductRequest.ProductUploadDto request) {
        Product new_product = productService.uploadProduct(productImage, detailImage, request);
        ProductResponse.ProductDto response = ProductConverter.toProductResponse(new_product);
        return ApiResponse.onSuccess(response);
    }

    @PutMapping("/{id}")
    @Operation(summary="상품 정보 수정")
    public ApiResponse<ProductResponse.ProductUpdateResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequest.ProductUpdateRequestDTO request) {

        ProductResponse.ProductUpdateResponseDTO response = productService.updateProduct(id, request);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="상품 삭제")
    public ApiResponse<ProductResponse.ProductDeleteResponseDTO> deleteProduct(@PathVariable Long id) {
        ProductResponse.ProductDeleteResponseDTO response = productService.deleteProduct(id);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("background-image")
    @Operation(summary="배경 이미지 생성")
    public ApiResponse<ProductResponse.ImageDTO> createBackgroundImage(@RequestBody @Valid ProductRequest.CreateBgImgDTO request) {
        ProductResponse.ImageDTO response = productService.createBackgroundImage(request);
        return ApiResponse.onSuccess(response);
    }
    @PostMapping("composite-image")
    @Operation(summary="합성 이미지 생성")
    public ApiResponse<ProductResponse.ImageDTO> createCompositeImage(@RequestBody @Valid ProductRequest.CreateCompositeImgDTO request) {
        ProductResponse.ImageDTO response = productService.createCompositeImage(request);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/likes/{product-id}")
    @Operation(summary="상품 좋아요 추가")
    public ApiResponse<ProductResponse.ProductLikeDTO> addLike(@AuthenticationPrincipal User user, @PathVariable("product-id")Long id) {
        ProductResponse.ProductLikeDTO response = productService.addLike(user, id);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/likes/{product-id}")
    @Operation(summary="상품 좋아요 삭제")
    public ApiResponse<ProductResponse.ProductLikeDTO> removeLike(@AuthenticationPrincipal User user, @PathVariable("product-id") Long id) {
        ProductResponse.ProductLikeDTO response = productService.removeLike(user, id);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/category/{category_id}")
    @Operation(summary="태그 ID로 제품 목록 조회")
    public ApiResponse<List<ProductResponse.ProductListDto>> getProductsByCategory(@PathVariable Long category_id) {
        List<ProductResponse.ProductListDto> response = productService.getProductsByCategory(category_id);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/search")
    @Operation(summary = "검색어 기반 상품 조회", description = "검색어가 상품 이름 또는 브랜드 이름에 포함된 상품을 조회합니다.")
    public ApiResponse<List<ProductResponse.ProductListDto>> searchProducts (@RequestParam String keyword) {
        List<ProductResponse.ProductListDto> response = productService.searchProducts(keyword);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/likes/{product-id}")
    @Operation(summary = "상품 좋아요 여부 확인")
    public ApiResponse<ProductResponse.ProductLikeDTO> isLike(@AuthenticationPrincipal User user, @PathVariable("product-id") Long id) {
        if (user == null) throw new ProductHandler(ErrorStatus.PRODUCT_NOT_LIKED);
        ProductResponse.ProductLikeDTO response = productService.isLike(user, id);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping(value = "/test/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드 테스트")
    public ApiResponse<String> uploadTest(@RequestPart MultipartFile file) {
        return ApiResponse.onSuccess(s3Service.uploadFile(file));
    }

    @PostMapping(value = "/test/delete")
    @Operation(summary = "이미지 삭제 테스트", description = "\"\" 없이 url만 입력해주세요.")
    public ApiResponse<String> deleteTest(@RequestBody String fileUrl) {
        s3Service.deleteImage(fileUrl);
        return ApiResponse.onSuccess(fileUrl);
    }
}
