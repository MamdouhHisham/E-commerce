package com.example.productservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final Cloudinary cloudinary;

    public Product saveProduct(ProductDTO productRequest, MultipartFile imageFile) throws IOException {

        Category category = categoryService.findCategoryById(productRequest.getCategoryId());

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setSkuCode(productRequest.getSkuCode());
        product.setCategory(category);

        // creates the parameters for the upload
        Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());

        // extract and save the public url from response
        String imgUrl = (String) uploadResult.get("url");
        product.setImageUrl(imgUrl);

        return productRepository.save(product);
    }

    public Product getProductById(long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
