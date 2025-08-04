package com.example.ecommerce.controller;


import com.example.ecommerce.dto.cart.OrderItemRequest;

import com.example.ecommerce.dto.cart.OrderResponse;
import com.example.ecommerce.dto.payment.PaymentRequest;
import com.example.ecommerce.entity.Order;

import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('BUYER', 'SELLER')")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @GetMapping
    public ResponseEntity<OrderResponse> getCart(Principal principal, @ParameterObject Pageable pageable){
        OrderResponse response = cartService.getCart(principal.getName(), pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<OrderResponse> addToCart(@Valid @RequestBody OrderItemRequest request, Principal principal){
        Order order = cartService.addToCart(
                principal.getName(),
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.toFullOrderResponse(order));
    }
    // 3. DELETE /api/cart/remove/{productId}
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<OrderResponse> removeItem(@PathVariable Long productId, Principal principal) {
        Order order = cartService.removeItemFromCart(principal.getName(), productId);
        return ResponseEntity.ok(cartMapper.toFullOrderResponse(order));
    }

    // 4. DELETE /api/cart/remove-all
    @DeleteMapping("/remove-all")
    public ResponseEntity<OrderResponse> clearCart(Principal principal) {
        Order order = cartService.clearCart(principal.getName());
        return ResponseEntity.ok(cartMapper.toFullOrderResponse(order));
    }

    // 5. POST /api/cart/checkout
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(Principal principal) {
        Order order = cartService.checkout(principal.getName());
        return ResponseEntity.ok(cartMapper.toFullOrderResponse(order));
    }


    @PostMapping("/pay")
    public ResponseEntity<OrderResponse> pay(@RequestParam("method")Order.PaymentMethod method, Principal principal){
        Order order = cartService.processPaymentAfter(principal.getName(), method);
        return ResponseEntity.ok(cartMapper.toFullOrderResponse(order));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<OrderResponse> payOrder(@PathVariable Long orderId,
                                                  @RequestBody PaymentRequest request,
                                                  Principal principal) {
        Order order = cartService.processPayment(principal.getName(), orderId, request.getMethod());
        return ResponseEntity.ok(cartMapper.toFullOrderResponse(order));
    }

}
