package com.ecommerce.project.service;

import com.ecommerce.project.Repository.ICatergoryRespository;
import com.ecommerce.project.Repository.IProductRepository;
import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
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

    @Autowired
    FileServiceImplementation fileServiceImplementation;

    @Value("${project.image}")
    private  String path;


    @Override
    public ProductDTO addProduct(ProductDTO productDto, Long categoryId) {

        //Check if product is already present or not

        boolean isProductNotPresent = true;

      Category category = catergoryRespository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Catergory", "categoryId", categoryId));

      List<Product> products = category.getProducts();

      for (Product product : products){
          if(product.getProductName().equals(productDto.getProductName())){
              isProductNotPresent = false;
              break;
          }
      }


        if(isProductNotPresent) {
            Product product = modelMapper.map(productDto, Product.class);

            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    ((product.getDiscount() * 0.01) * product.getPrice());
            product.setImage("default.png");
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new ApiException("Product Already Present");
        }
    }

    @Override
    public ProductResponseDTO getAllProduct(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {


        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize, sortByAndOrder);

        Page<Product> pageProducts = productRepository.findAll(pageable);



       List<Product> products =  pageProducts.getContent();

      List<ProductDTO> productDTOS= products.stream()
              .map(product -> modelMapper.map(product, ProductDTO.class))
              .collect(Collectors.toList());

      if(products.size() == 0){
          throw  new ApiException("No Product Present");
      }


        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productDTOS);
        productResponseDTO.setPageNumber(pageProducts.getNumber());
        productResponseDTO.setLastPage(pageProducts.isLast());
        productResponseDTO.setTotalElements(pageProducts.getTotalElements());
        productResponseDTO.setTotalPages(pageProducts.getTotalPages());
        return  productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();


        Category category = catergoryRespository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Catergory", "categoryId", categoryId));

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByOrder);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()){
            throw  new ApiException(category.getCategoryName() + " not found for the Product");
        }


        List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();

        productResponseDTO.setContent(productDTOS);
        productResponseDTO.setPageNumber(productPage.getNumber());
        productResponseDTO.setLastPage(productPage.isLast());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setContent(productDTOS);
        return  productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%", pageDetails);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()){
            throw  new ApiException("Product not found with " + keyword);
        }

        List<ProductDTO> productDTOS= products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setPageNumber(productPage.getNumber());
        productResponseDTO.setLastPage(productPage.isLast());
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setContent(productDTOS);
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

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //Product from Db
        Product productFromDb  = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        //Upload image to server
        // Get the file name of the uploaded image
        String fileName = fileServiceImplementation.uploadImage(path, image);

        // Updating the new file name to the product
        productFromDb.setImage(fileName);

        //save updated Product
        Product savedProduct = productRepository.save(productFromDb);

        // return DTO after mapping product to DTO
        return  modelMapper.map(savedProduct, ProductDTO.class);

    }


}
