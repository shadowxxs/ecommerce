package com.example.ecommerce.dto.auth;


import com.example.ecommerce.entity.User;
import lombok.Data;


@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private User.Role role;
}
