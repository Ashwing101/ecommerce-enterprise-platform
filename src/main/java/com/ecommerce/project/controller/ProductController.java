package com.ecommerce.project.controller;


import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import com.ecommerce.project.service.ProductServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

        @Autowired
        ProductServiceImplementation productServiceImplementation;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product, @PathVariable Long categoryId){

        ProductDTO savedProductDTO =  productServiceImplementation.addProduct(product, categoryId);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProduct(){

        ProductResponseDTO productResponseDTO =  productServiceImplementation.getAllProduct();
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductByCategory(@PathVariable Long categoryId){

        ProductResponseDTO productResponseDTO =  productServiceImplementation.getProductByCategory(categoryId);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/public/product/keyword/{keyword}")
    public ResponseEntity<ProductResponseDTO> getProductByKeyword(@PathVariable String keyword){

        ProductResponseDTO productResponseDTO =  productServiceImplementation.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO product, @PathVariable Long productId){

        ProductDTO productDTO =  productServiceImplementation.updateProductByProductId(product, productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productServiceImplementation.deleteProduct(productId);
        return  new ResponseEntity<>(productDTO, HttpStatus.OK);
    }




}
