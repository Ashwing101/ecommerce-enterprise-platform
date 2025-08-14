package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoryService {

     List<Category> getAllCategories();
     void createCategory(Category category);
     String deleteCategory(long category);

      ResponseEntity<String> updateCategory (Category categoryData, long categoryId);
}
