package com.example.productservice.service;

import com.example.productservice.model.Category;
import com.example.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category){
        if(categoryRepository.existsByName(category.getName())){
            throw new RuntimeException("Category " + category.getName() + " already exists");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category){
        Category current = findCategoryById(category.getId());

        current.setName(category.getName());
        current.setDescription(category.getDescription());
        current.setProducts(category.getProducts());

        return categoryRepository.save(current);
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
