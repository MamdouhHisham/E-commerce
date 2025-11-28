package com.example.productservice.service;

import com.example.productservice.exceptions.DuplicateResourceException;
import com.example.productservice.exceptions.ResourceNotFoundException;
import com.example.productservice.model.Category;
import com.example.productservice.model.Product;
import com.example.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategoryById(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        log.info("Fetching category with ID: {}", id);
        if(category.isPresent()){
            log.debug("Category found: {}", category.get().getName());
            return category.get();
        } else {
            log.error("Category not found with ID: {}", id);
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
    }

    public List<Category> findAll(){
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        log.info("Retrieved {} categories", categories.size());
        return categories;
    }

    public Category addCategory(Category category){
        log.info("Request to add new category: {}", category.getName());

        if(categoryRepository.existsByName(category.getName())){
            log.warn("Attempted to add duplicate category: {}", category.getName());
            throw new DuplicateResourceException("Category with name " + category.getName() + " already exists");
        }

        Category savedCategory = categoryRepository.save(category);

        log.info("Category saved successfully with ID: {}", savedCategory.getId());

        return savedCategory;
    }

    public Category updateCategory(Category category){
        log.info("Request to update category with ID: {}", category.getId());

        Category current = findCategoryById(category.getId());

        if(!current.getName().equals(category.getName()) && categoryRepository.existsByName(category.getName())){
            log.warn("Cannot update category. Name '{}' already exists", category.getName());
            throw new DuplicateResourceException("Category with name " + category.getName() + " already exists");
        }

        current.setName(category.getName());
        current.setDescription(category.getDescription());
        current.setProducts(category.getProducts());

        Category updatedCategory = categoryRepository.save(current);
        log.info("Category updated successfully: {}", updatedCategory.getId());

        return updatedCategory;
    }

    public void deleteCategory(Long id){
        log.info("Request to delete category with ID: {}", id);

        if(!categoryRepository.existsById(id)){
            log.error("Cannot delete. Category not found with ID: {}", id);
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);

        log.info("Category deleted successfully: {}", id);
    }
}
