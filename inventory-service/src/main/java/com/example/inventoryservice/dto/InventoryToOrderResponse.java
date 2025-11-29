package com.example.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class InventoryToOrderResponse {
    private String skuCode;
    private Boolean isInStock;
}
