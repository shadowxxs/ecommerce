package com.example.ecommerce.dto.cart;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
}
