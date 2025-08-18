package com.ecommerce.project.service;

import com.ecommerce.project.Repository.ICatergoryRespository;
import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;



@Service
public class CategoryServiceImpl implements  ICategoryService{

    @Autowired
    private ICatergoryRespository  catergoryRespository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
       //Sort of Pagination Data
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //For Pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = catergoryRespository.findAll(pageDetails);
        List<Category> categoryList =  categoryPage.getContent();
      //  List<Category> categoryList =  catergoryRespository.findAll();
        if(categoryList.isEmpty())
             throw new ApiException("No Categories is added till now!!!");

        List<CategoryDTO> categoryDTO = categoryList.stream().
        map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponseDTO  categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(categoryDTO);
        categoryResponseDTO.setLastPage(categoryPage.isLast());
        categoryResponseDTO.setPageNumber(categoryPage.getNumber());
        categoryResponseDTO.setTotalElements(categoryPage.getTotalElements());
        categoryResponseDTO.setPageSize(categoryPage.getSize());
        return categoryResponseDTO;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryDTO category) {
        Category existing = catergoryRespository.findByCategoryName(category.getCategoryName());
        Category mapper = modelMapper.map(category, Category.class);
        if (existing != null) {
            throw new ApiException("Category with this name " + category.getCategoryName() + " already exists!");
        }
        Category saved = catergoryRespository.save(mapper);

        CategoryDTO dto = modelMapper.map(saved, CategoryDTO.class);
        CategoryResponseDTO resp = new CategoryResponseDTO();
        resp.setContent(List.of(dto));  // wrap single item into the list
        return resp;
    }

    @Override
    public CategoryResponseDTO deleteCategory(long id) {

//        Category category = catergoryRespository.findById(id)
//        .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));
//
//        Category category = categories.stream()
//        .filter(n -> n.getCategoryId().equals(id))
//        .findFirst()
//        .orElseThrow(()-> new ResponseStatusException(NOT_FOUND, "Resource not found"));

        Category category = catergoryRespository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Category", "CatergoryId", id)); // Custom Excpetion

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        CategoryResponseDTO resp = new CategoryResponseDTO();
        resp.setContent(List.of(categoryDTO));
        catergoryRespository.delete(category);
        return  resp;

    }

//    @Override  this code is optimize in the below code
//    public Category updateCategory(Category categoryData, long categoryId) {
//        List<Category> categories = catergoryRespository.findAll();
//
//       Optional<Category> categoryStream =  categories.stream()
//                 .filter(n -> n.getCategoryId().equals(categoryId))
//                 .findFirst();
//
//           if(categoryStream.isPresent()){
//               Category existingCategory = categoryStream.get();
//               existingCategory.setCategoryName(categoryData.getCategoryName());
//               Category saveCategory = catergoryRespository.save(existingCategory);
//               return saveCategory;
//              // return new ResponseEntity<>("Category with categoryId \" + categoryId + \" is Updated Successfully", HttpStatus.OK);
//           }else {
//               throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found for categoryId " + categoryId);
//             //  return  "Category with categoryId " + categoryId + " is not found";
//           }
//    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryDTO categoryData, long categoryId) {
        Optional<Category> savedCategoryOptional = catergoryRespository.findById(categoryId);

        Category savedCategory = savedCategoryOptional
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryData.setCategoryId(categoryId);
        savedCategory = catergoryRespository.save(modelMapper.map(categoryData, Category.class));
        CategoryDTO categoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        CategoryResponseDTO resp = new CategoryResponseDTO();
        resp.setContent(List.of(categoryDTO));
        return resp;


    }

}
