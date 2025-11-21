package com.example.productservice.controller;

import com.example.productservice.dto.ProductDTO;
import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @RequestParam("image")MultipartFile image,
            @RequestParam("product") String productJson
            ) {
        try {
            ProductDTO product = objectMapper.readValue(productJson, ProductDTO.class);
            Product newProduct = productService.saveProduct(product, image);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("error: " + e.getMessage());
        }
    }

}
