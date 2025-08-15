package com.ecommerce.project.controller;


import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ecom/")
public class CatergoryController {


    private CategoryServiceImpl categoryService;

    public CatergoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("api/public/categories")
    public  ResponseEntity<CategoryResponseDTO> getAllCategories(){
        CategoryResponseDTO categoryResponseDTO = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }

    @PostMapping("api/public/category")
    public  ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryDTO category){
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(category);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.CREATED);
        //return  "Category Save Successfully";
    }
    @DeleteMapping("api/public/category/{id}")
    public ResponseEntity<CategoryResponseDTO> deleteCategory(@PathVariable  long id) {

        CategoryResponseDTO categoryResponseDTO =  categoryService.deleteCategory(id);
            return  new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
            //ResponseEntity.ok(status); // All three ways are correct of using response Entity
           // return  ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @PutMapping("api/public/category/{categoryId}")
    public  ResponseEntity<CategoryResponseDTO> updateCategory(@Valid @RequestBody CategoryDTO category, @PathVariable long categoryId){
        CategoryResponseDTO categoryResponseDTO = categoryService.updateCategory(category, categoryId);
            return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }
}
