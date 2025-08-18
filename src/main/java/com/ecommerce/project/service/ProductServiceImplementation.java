package com.ecommerce.project.service;

import com.ecommerce.project.Repository.ICatergoryRespository;
import com.ecommerce.project.Repository.IProductRepository;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements IProductService {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    ICatergoryRespository catergoryRespository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {

      Category category = catergoryRespository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Catergory", "categoryId", categoryId));

      Product product  = modelMapper.map(productDto, Product.class);

        product.setCategory(category);
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01) *  product.getPrice());
        product.setImage("default.png");
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponseDTO getAllProduct() {
       List<Product> products =  productRepository.findAll();

      List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);
        return  productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductByCategory(Long categoryId) {

        Category category = catergoryRespository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Catergory", "categoryId", categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);
        return  productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");

        List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);
        return  productResponseDTO;
    }


    public ProductDTO updateProductByProductId( ProductDTO productDto, Long productId) {

 Product productFromDb = productRepository.findById(productId).orElseThrow(
         () -> new ResourceNotFoundException("Product", "PorductId", productId));

 Product product = modelMapper.map(productDto, Product.class);

        productFromDb.setProductName(product.getProductName());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        Product savedProduct = productRepository.save(productFromDb);

        ProductDTO responeProductDto =   modelMapper.map(savedProduct, ProductDTO.class);

        return responeProductDto;
    }

    public ProductDTO deleteProduct(Long productId) {

        Product productFromDb = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "PorductId", productId));

             productRepository.deleteById(productId);
             ProductDTO productDTO = modelMapper.map(productFromDb, ProductDTO.class);
             return  productDTO;
    }
}
