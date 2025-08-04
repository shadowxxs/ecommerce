package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.cart.OrderItemRequest;
import com.example.ecommerce.dto.cart.OrderItemResponse;
import com.example.ecommerce.dto.cart.OrderResponse;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

   public OrderResponse toFullOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream().map(this::toOrderItemResponse).toList();

        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .username(order.getUser().getUsername())
                .method(order.getPaymentMethod())
                .items(items)
                .build();
    }

    public OrderResponse toPageOrderResponse(Order order, List<OrderItemResponse> pagedItems){
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .items(pagedItems)
                .build();
    }

    public OrderItemRequest toOrderItemRequest(OrderItem item) {
        return OrderItemRequest.builder()
                .productId(item.getId())
                .quantity(item.getQuantity())
                .build();
    }

}
