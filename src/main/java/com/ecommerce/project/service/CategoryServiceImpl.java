package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CategoryServiceImpl implements  ICategoryService{

    public List<Category> categories = new ArrayList<>();
    private long id =1;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(id++);
        categories.add(category);

    }

    @Override
    public String deleteCategory(long id) {
        Category category = categories.stream()
                .filter(n -> n.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(NOT_FOUND, "Resource not found"));
//
//        if(category == null){
//            return  "Category not found";
//        }
        categories.remove(category);
        return  "Category with categoryId " + id + " is removed sucessfully";

    }

    @Override
    public ResponseEntity<String> updateCategory(Category categoryData, long categoryId) {

       Optional<Category> categoryStream =  categories.stream()
                 .filter(n -> n.getCategoryId().equals(categoryId))
                 .findFirst();

           if(categoryStream.isPresent()){
               Category existingCategory = categoryStream.get();
               existingCategory.setCategoryName(categoryData.getCategoryName());
               return new ResponseEntity<>("Category with categoryId \" + categoryId + \" is Updated Successfully", HttpStatus.OK);
           }else {
               return new ResponseEntity<>("Category with categoryId " + categoryId + " is not found", HttpStatus.NOT_FOUND);
             //  return  "Category with categoryId " + categoryId + " is not found";
           }
    }
}
