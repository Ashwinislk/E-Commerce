package com.electronicstore.service.Impl;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.entity.Category;
import com.electronicstore.entity.Product;
import com.electronicstore.exception.ResourceNotFound;
import com.electronicstore.payload.Helper;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.ProductDto;
import com.electronicstore.repository.CategoryRepository;
import com.electronicstore.repository.ProductRepository;
import com.electronicstore.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        log.info("Entering the dao call for create product data");
        Product product = this.modelMapper.map(productDto, Product.class);
        Date date = new Date();
        String id = UUID.randomUUID().toString();
        product.setProductId(id);
        product.setAddedDate(date);

        Product newproduct = this.productRepository.save(product);
        log.info("Completed the dao call for create product data");
        return modelMapper.map(newproduct, ProductDto.class);
    }

    @Override
    public ProductDto getSingleProduct(String productId) {
        log.info("Entering the dao call for get single product data with productId:{}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + productId));
        log.info("Completed the dao call for get single product data with productId:{}",productId);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        log.info("Entering the dao call for update product data with productId:{}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + productId));
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setDiscountPrice(productDto.getDiscountPrice());
        product.setLive(productDto.getLive());
        product.setStock(productDto.getStock());
        Product saveProduct = this.productRepository.save(product);
        log.info("Completed the dao call for update product data with productId:{}",productId);
        return modelMapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        log.info("Entering the dao call for delete product data with productId:{}",productId);
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + productId));
        productRepository.delete(product);
        log.info("Completed the dao call for delete product data with productId:{}",productId);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        log.info("Entering the dao call for get all products with the pagination");
        Sort sort = (direction.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        PageRequest pages = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> all = productRepository.findAll(pages);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(all, ProductDto.class);
        log.info("Completed the dao call for get all products with the pagination");
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> findByLiveTrue(Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        log.info("Entering the dao call for find by live true with the pagination");
        Sort sort;
        if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        PageRequest pages = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> byLiveTrue = productRepository.findByLiveTrue(pages);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byLiveTrue, ProductDto.class);
        log.info("Completed the dao call for find by live true with the pagination");
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProducts(Integer pageNumber, Integer pageSize, String sortBy, String direction) {
        log.info("Entering the dao call for get all live products with the pagination");
        Sort sort = (direction.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        PageRequest pages = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> products = productRepository.findAll(pages);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(products, ProductDto.class);
        log.info("Completed the dao call for get all live products with the pagination");
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> findProductsByTitle(Integer pageNumber, Integer pageSize, String sortBy, String direction,String keyword) {
        log.info("Entering the dao call for find product by title with the pagination");
        Sort sort = (direction.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        PageRequest pages = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> byTitleContaining = productRepository.findByTitleContaining(pages, keyword);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byTitleContaining, ProductDto.class);
        log.info("Entering the dao call for find products by title with the pagination");
        return pageableResponse;
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
        log.info("Entering the dao call for create product with category with categoryId:{}",categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + categoryId));
        Product product = this.modelMapper.map(productDto, Product.class);

        Date date=new Date();
        String id = UUID.randomUUID().toString();
        product.setProductId(id);
        product.setAddedDate(date);
        product.setCategory(category);
        Product product1 = this.productRepository.save(product);
        log.info("Completed the dao call for create product with category with categoryId:{}",categoryId);
        return modelMapper.map(product1,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Entering the dao call for get all of products with categoty with category with categoryId:{}",categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + categoryId));
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        PageRequest pages = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> byCategories = this.productRepository.findByCategory(category, pages);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(byCategories, ProductDto.class);
        log.info("Completed the dao call for get all of products with categoty with category with categoryId:{}",categoryId);
        return pageableResponse;
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        log.info("Entering the dao call for update product with categoty with categoryId and productId:{}:{}",categoryId,productId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + categoryId));
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + productId));
        product.setCategory(category);
        Product save = this.productRepository.save(product);
        ProductDto dto = modelMapper.map(save, ProductDto.class);
        log.info("Completed the dao call for update product with categoty with categoryId and productId:{}:{}",categoryId,productId);
        return dto;
    }
}
