package com.example.ecommerce.dto.cart;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    OrderItemRequest {

    private Long productId;
    private Integer quantity;
}
