package com.example.ecommerce.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
}
