package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.product.ProductRequest;
import com.example.ecommerce.dto.product.ProductResponse;
import com.example.ecommerce.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toRequest(ProductRequest request){
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

    public ProductResponse toResponse(Product product) {
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
