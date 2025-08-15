package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;


public interface ICategoryService {

     CategoryResponseDTO getAllCategories();
     CategoryResponseDTO createCategory(CategoryDTO category);
     CategoryResponseDTO deleteCategory(long category);
     CategoryResponseDTO updateCategory (CategoryDTO categoryData, long categoryId);
}
