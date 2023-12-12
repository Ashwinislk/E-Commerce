package com.electronicstore.service;

import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.ProductDto;

public interface ProductService {

    public ProductDto saveProduct(ProductDto productDto);

    public ProductDto getSingleProduct(String productId);

    public ProductDto updateProduct(ProductDto productDto, String productId);

    void deleteProduct(String prodyctId);

    PageableResponse<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String direction);

    PageableResponse<ProductDto> findByLiveTrue(Integer pageNumber, Integer pageSize, String sortBy, String direction);

    PageableResponse<ProductDto> getAllLiveProducts(Integer pageNumber, Integer pageSize, String sortBy, String direction);

    PageableResponse<ProductDto> findProductsByTitle(Integer pageNumber, Integer pageSize, String sortBy, String direction,String keyword);

    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);

    PageableResponse<ProductDto> getAllOfCategory(String categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

    ProductDto updateCategory(String productId,String categoryId);


}
