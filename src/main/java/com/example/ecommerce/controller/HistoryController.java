    package com.example.ecommerce.controller;



import com.example.ecommerce.dto.cart.OrderResponse;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("isAuthenticated()")
public class HistoryController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;



    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getUserOrderHistory(@ParameterObject Pageable pageable, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Order> orders = orderRepository.findByUser(user, pageable);
        Page<OrderResponse> responses = orders.map(cartMapper::toFullOrderResponse);

        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable Long orderId, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found or not yours"));

        return ResponseEntity.ok(cartMapper.toFullOrderResponse(order));
    }
}
