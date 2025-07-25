package com.example.ecommerce.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private long id;
    private String name;
    private String type;
    private Double price;
    private String description;
    private String imageUrl;
    private Integer stock;
    private Boolean isActive;

}
