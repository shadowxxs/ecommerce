package com.example.ecommerce.dto.payment;


import com.example.ecommerce.entity.Order;
import lombok.Data;

@Data
public class PaymentRequest {

    private Order.PaymentMethod method;
}
