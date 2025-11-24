package com.example.productservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDTO {
    @NotBlank(message = "Product name cannot be blank") @NotEmpty
    private String name;

    @NotBlank(message = "Product description cannot be blank") @NotEmpty
    private String description;

    @Positive(message = "Product price must be positive") @Min(value = 1, message = "Product price must be at least 1")
    @NotEmpty
    private double price;

    @NotBlank(message = "Product SKU code cannot be blank") @NotEmpty
    private String skuCode;

    @NotNull(message = "Category ID cannot be null") @NotEmpty @Min(value = 1)
    private Long categoryId;

    @NotNull(message = "Quantity cannot be null") @Min(value = 1) @NotEmpty
    private Long quantity;
}
