package com.example.ecommerce.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "product_tbl")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double price;

    @Column(length = 500)
    private String description;

    private String imageUrl;

    private Integer stock;

    private Boolean isActive;
}
