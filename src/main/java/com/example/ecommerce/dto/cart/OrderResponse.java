package com.example.ecommerce.dto.cart;




import com.example.ecommerce.entity.Order;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private String username;
    private Order.PaymentMethod method;
}
