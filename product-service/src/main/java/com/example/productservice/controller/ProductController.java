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

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @RequestParam("image")MultipartFile image,
            @RequestParam("product") String productJson
            ) {
        try {
            ProductDTO product = objectMapper.readValue(productJson, ProductDTO.class);
            Product newProduct = productService.addProduct(product, image);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("error: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("product") String productJson
    ){
        try {
            ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);
            Product updateProduct = productService.updateProduct(id, productDTO, image);

            return ResponseEntity.ok(updateProduct);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product is deleted");
    }

}
