package com.example.ecommerce.dto.cart;

import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private List<OrderItemRequest> items;
    private String address;
}
