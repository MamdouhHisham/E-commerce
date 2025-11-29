package com.example.productservice.dto;

import lombok.Data;

@Data
public class ProductToOrderResponse {
    private String skuCode;
    private Double price;
}
