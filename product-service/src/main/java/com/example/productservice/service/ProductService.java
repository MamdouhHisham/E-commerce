package com.example.productservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.client.InventoryClient;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final Cloudinary cloudinary;
    private final InventoryClient inventoryClient;

    public Product getProductById(long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(ProductDTO productRequest, MultipartFile image) throws IOException {

        Category category = categoryService.findCategoryById(productRequest.getCategoryId());

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setSkuCode(productRequest.getSkuCode());
        product.setCategory(category);

        // creates the parameters for the upload
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());

        // extract and save the public url from response
        String imgUrl = (String) uploadResult.get("url");
        product.setImageUrl(imgUrl);

        // add product sku code and his quantity in inventory
        inventoryClient.addProductToInventory(productRequest.getSkuCode(), productRequest.getQuantity());

        return productRepository.save(product);
    }

    // private method for delete image from cloud
    private void deleteImageFromCloud(String imageUrl){
        try {
            if(imageUrl == null || imageUrl.isEmpty()) return;

            int lastSlashIdx = imageUrl.lastIndexOf("/");
            int lastDotIdx = imageUrl.lastIndexOf(".");

            String Id = imageUrl.substring(lastSlashIdx + 1, lastDotIdx);

            cloudinary.uploader().destroy(Id, ObjectUtils.emptyMap());
        } catch (IOException e){
            System.err.println("Failed to delete image from cloud");
        }
    }

    public Product updateProduct(Long id, ProductDTO productDTO, MultipartFile image) throws IOException {

        Product current = getProductById(id);

        // update if data provided in DTO
        if(productDTO.getName() != null) current.setName(productDTO.getName());
        if(productDTO.getDescription() != null) current.setDescription(productDTO.getDescription());
        if(productDTO.getPrice() > 0) current.setPrice(productDTO.getPrice());
        if(productDTO.getSkuCode() != null) current.setSkuCode(productDTO.getSkuCode());

        if(productDTO.getCategoryId() != null){
            Category category = categoryService.findCategoryById(productDTO.getCategoryId());
            current.setCategory(category);
        }

        if(image != null && !image.isEmpty()){

            deleteImageFromCloud(current.getImageUrl());

            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imgUrl = (String) uploadResult.get("url");
            current.setImageUrl(imgUrl);
        }

        return productRepository.save(current);
    }

    public void deleteProduct(Long id){
        Product product = getProductById(id);
        deleteImageFromCloud(product.getImageUrl());
        productRepository.deleteById(id);
    }
}
