package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponseDTO;


public interface IProductService {
    ProductDTO addProduct (ProductDTO product, Long categoryId);

    ProductResponseDTO getAllProduct();

    ProductResponseDTO getProductByCategory(Long categoryId);


    ProductResponseDTO searchProductByKeyword(String keyword);

    ProductDTO updateProductByProductId( ProductDTO product, Long productId);

    ProductDTO deleteProduct(Long productId);
}
