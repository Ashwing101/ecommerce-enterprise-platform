package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IProductService {
    ProductDTO addProduct (ProductDTO product, Long categoryId);

    ProductResponseDTO getAllProduct(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);

    ProductResponseDTO getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    ProductResponseDTO searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);

    ProductDTO updateProductByProductId( ProductDTO product, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
