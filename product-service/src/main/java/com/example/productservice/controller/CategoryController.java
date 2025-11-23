package com.example.productservice.controller;


import com.example.productservice.model.Category;
import com.example.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.addCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted");
    }

}
