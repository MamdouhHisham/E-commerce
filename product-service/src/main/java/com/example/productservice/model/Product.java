package com.example.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name =  "product")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private double price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "sku_code")
    private String skuCode;

}
