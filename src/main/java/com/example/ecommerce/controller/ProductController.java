package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProduct(Pageable pageable){
        Page<ProductResponse> responsePage = productService.getAllProduct(pageable).map(this::mapToResponse);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(mapToResponse(product));
    }

    @GetMapping("/search/name")
    public ResponseEntity<Page<ProductResponse>> getProductByName(@RequestParam String name, Pageable pageable){
        Page<ProductResponse> responsePage = productService.getProductByName(name, pageable).map(this::mapToResponse);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/search/type")
    public ResponseEntity<Page<ProductResponse>> getProductByType(@RequestParam String type, Pageable pageable){
        Page<ProductResponse> responsePage = productService.getProductByType(type, pageable).map(this::mapToResponse);
        return ResponseEntity.ok(responsePage);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid  @RequestBody ProductRequest request) {
        Product saved = productService.createProduct(mapToRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
       Product updated = productService.updateProduct(id, mapToRequest(request));
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Mapping from DTO -> Entity
    private Product mapToRequest(ProductRequest request){
        return Product.builder()
                .name(request.getName())
                .type(request.getType())
                .price(request.getPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .stock(request.getStock())
                .isActive(request.getIsActive())
                .build();
    }


    //Mapping from Entity -> DTO response
    private ProductResponse mapToResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .type(product.getType())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .stock(product.getStock())
                .isActive(product.getIsActive())
                .build();
    }
}
