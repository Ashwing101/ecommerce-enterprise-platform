package com.ecommerce.project.controller;


import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryServiceImpl;
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
    public  ResponseEntity<List<Category>> getAllCategories(){
        List<Category> ls = categoryService.getAllCategories();
        return new ResponseEntity<>(ls, HttpStatus.OK);
    }

    @PostMapping("api/public/category")
    public  ResponseEntity<String> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return  new ResponseEntity<>("Category Save Successfully", HttpStatus.CREATED);
        //return  "Category Save Successfully";
    }
    @DeleteMapping("api/public/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable  long id) {
        try {
            String status =  categoryService.deleteCategory(id);
            return  new ResponseEntity<>(status, HttpStatus.OK);
            //ResponseEntity.ok(status); // All three ways are correct of using response Entity
           // return  ResponseEntity.status(HttpStatus.OK).body(status);

        }catch (ResponseStatusException ex){
            return  new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        }

    }

    @PutMapping("api/public/category/{categoryId}")
    public  ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable long categoryId){

        try{
            ResponseEntity<String> status = categoryService.updateCategory(category, categoryId);
            return  new ResponseEntity<>("" , HttpStatus.OK);
        }catch(ResponseStatusException ex){
                new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        }
        return null;

    }


}
