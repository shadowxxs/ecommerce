package com.example.ecommerce.controller;


import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.dto.SignupRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.security.jwt.JwtUtil;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;


    //Endpoint Register
    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        User savedUser = userService.registerUser(request);
        return ResponseEntity.ok(savedUser);
    }

    //Endpoint Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(request.getUsername());


            return ResponseEntity.ok(new AuthResponse(token, "Login Berhasil"));
        } catch (AuthenticationException e){
            return ResponseEntity.status(401).body("Username atau Password salah");
        }
    }

    }
