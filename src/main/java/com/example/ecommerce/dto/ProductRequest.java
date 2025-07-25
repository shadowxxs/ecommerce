package com.example.ecommerce.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Nama product tidak boleh kosong")
    private String name;

    @NotBlank(message = "Type product tidak boleh kosong")
    private String type;

    @NotNull(message = "Harga product harus diisi")
    @Min(value = 0, message = "Harga tidak boleh negatif")
    private Double price;

    private String description;
    private String imageUrl;

    @NotNull(message = "Stock harus diisi")
    @Min(value = 0, message = "Stock tidak boleh negatif")
    private Integer stock;


    private Boolean isActive = true;
}
