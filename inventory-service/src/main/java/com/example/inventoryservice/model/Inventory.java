package com.example.inventoryservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "sku_code", unique = true)
    private String skuCode;

    private Long quantity;
}
