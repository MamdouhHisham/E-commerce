package com.example.productservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.client.InventoryClient;
import com.example.productservice.dto.ProductDTO;
import com.example.productservice.exceptions.ResourceNotFoundException;
import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final Cloudinary cloudinary;
    private final InventoryClient inventoryClient;

    public Product getProductById(long id){
        Optional<Product> product = productRepository.findById(id);
        log.info("Fetching product with ID: {}", id);
        if(product.isPresent()){
            log.debug("Product found: {}", product.get().getName());
            return product.get();
        } else {
            log.error("Product not found with ID: {}", id);
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }

    public List<Product> findAllProducts(){
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.info("Retrieved {} products", products.size());
        return products;
    }

    public Product addProduct(ProductDTO productRequest, MultipartFile image) throws IOException {

        log.info("Request to add new product: {}, SKU: {}", productRequest.getName(), productRequest.getSkuCode());

        Category category = categoryService.findCategoryById(productRequest.getCategoryId());

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setSkuCode(productRequest.getSkuCode());
        product.setCategory(category);

        try {
            log.info("Uploading image to Cloudinary for product: {}", productRequest.getName());
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imgUrl = (String) uploadResult.get("url");
            product.setImageUrl(imgUrl);
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw e;
        }

        log.info("Calling Inventory Service for SKU: {}", productRequest.getSkuCode());
        inventoryClient.addProductToInventory(productRequest.getSkuCode(), productRequest.getQuantity());

        Product savedProduct = productRepository.save(product);
        log.info("Product saved successfully with ID: {}", savedProduct.getId());

        return savedProduct;
    }

    // private method for delete image from cloud
    private void deleteImageFromCloud(String imageUrl){

        try {
            if(imageUrl == null || imageUrl.isEmpty()) {
                log.warn("Skipping image deletion: Image URL is null or empty");
                return;
            }

            int lastSlashIdx = imageUrl.lastIndexOf("/");
            int lastDotIdx = imageUrl.lastIndexOf(".");
            String publicId = imageUrl.substring(lastSlashIdx + 1, lastDotIdx);

            log.info("Deleting image from Cloudinary with Public ID: {}", publicId);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        } catch (IOException e){
            log.error("Failed to delete image from cloud for URL: {}", imageUrl, e);
        }
    }

    public Product updateProduct(Long id, ProductDTO productDTO, MultipartFile image) throws IOException {

        log.info("Request to update product with ID: {}", id);

        Product current = getProductById(id); //

        if(productDTO.getName() != null) current.setName(productDTO.getName());
        if(productDTO.getDescription() != null) current.setDescription(productDTO.getDescription());
        if(productDTO.getPrice() > 0) current.setPrice(productDTO.getPrice());
        if(productDTO.getSkuCode() != null) current.setSkuCode(productDTO.getSkuCode());

        if(productDTO.getCategoryId() != null){
            log.debug("Updating category for product ID: {}", id);
            Category category = categoryService.findCategoryById(productDTO.getCategoryId());
            current.setCategory(category);
        }

        if(image != null && !image.isEmpty()){
            log.info("Updating product image. Deleting old image");
            deleteImageFromCloud(current.getImageUrl());

            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imgUrl = (String) uploadResult.get("url");
            current.setImageUrl(imgUrl);
        }

        Product updatedProduct = productRepository.save(current);
        log.info("Product updated successfully: {}", updatedProduct.getId());

        return updatedProduct;
    }

    public void deleteProduct(Long id){
        log.info("Request to delete product with ID: {}", id);

        Product product = getProductById(id);

        log.info("Deleting associated image from cloud");
        deleteImageFromCloud(product.getImageUrl());

        productRepository.deleteById(id);
        log.info("Product with ID: {} deleted successfully", id);
    }
}
