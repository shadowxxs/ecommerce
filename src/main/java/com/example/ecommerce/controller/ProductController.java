package com.example.ecommerce.controller;

import com.example.ecommerce.dto.product.ProductRequest;
import com.example.ecommerce.dto.product.ProductResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProduct(@ParameterObject Pageable pageable){
        Page<ProductResponse> responsePage = productService.getAllProduct(pageable).map(productMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @GetMapping("/search/name")
    public ResponseEntity<Page<ProductResponse>> getProductByName(@RequestParam String name, @ParameterObject Pageable pageable){
        Page<ProductResponse> responsePage = productService.getProductByName(name, pageable).map(productMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/search/type")
    public ResponseEntity<Page<ProductResponse>> getProductByType(@RequestParam String type, @ParameterObject Pageable pageable){
        Page<ProductResponse> responsePage = productService.getProductByType(type, pageable).map(productMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid  @RequestBody ProductRequest request) {
//        String username = principal.getName();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getRole() != User.Role.ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
        Product saved = productService.createProduct(productMapper.toRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
//        String username = principal.getName();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getRole() != User.Role.ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
       Product updated = productService.updateProduct(id, productMapper.toRequest(request));
        return ResponseEntity.ok(productMapper.toResponse(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
//        String username = principal.getName();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getRole() != User.Role.ADMIN) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }





}
