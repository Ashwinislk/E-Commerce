package com.electronicstore.controller;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.payload.ApiResponse;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.ProductDto;
import com.electronicstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto productDto1 = this.productService.saveProduct(productDto);
        return new ResponseEntity<ProductDto>(productDto1, HttpStatus.CREATED);
    }

    @PutMapping("/products/update/productId/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId) {
        ProductDto productDto1 = this.productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    @GetMapping("/products/productId/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
        ProductDto singleProduct = this.productService.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<PageableResponse> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {

        PageableResponse<ProductDto> allProducts = this.productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @DeleteMapping("/products/productId/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {
        ApiResponse response = ApiResponse.builder().message(AppConstant.DELETE).status(false).httpcode(HttpStatus.OK).build();
        this.productService.deleteProduct(productId);

        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/products/livetrue")
    public ResponseEntity<PageableResponse> getByLiveTrue(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
        PageableResponse<ProductDto> byLiveTrue = this.productService.findByLiveTrue(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(byLiveTrue, HttpStatus.OK);
    }

    @GetMapping("/products/getAllLiveProducts")
    public ResponseEntity<PageableResponse> getAllLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {

        PageableResponse<ProductDto> allLiveProducts = this.productService.getAllLiveProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allLiveProducts, HttpStatus.OK);
    }

    @GetMapping("/products/productsByTitle/{keyword}")
    public ResponseEntity<PageableResponse> getByProductsByTitle(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir, @PathVariable String keyword) {
        PageableResponse<ProductDto> productsByTitle = this.productService.findProductsByTitle(pageNumber,pageSize,sortBy, sortDir,keyword);
        return new ResponseEntity<>(productsByTitle, HttpStatus.OK);

    }
   @PostMapping("/category/{categoryId}")
    public ResponseEntity<ProductDto> saveProductWithCategoryId(@PathVariable String categoryId,@RequestBody ProductDto productDto){

        ProductDto product = this.productService.createProductWithCategory(productDto, categoryId);
        return new ResponseEntity<>(product,HttpStatus.CREATED);

    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageableResponse<ProductDto>> getAllUsersCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "direction", defaultValue = AppConstant.SORT_DIR, required = false) String direction   ){

        PageableResponse<ProductDto> allOfCategory = this.productService.getAllOfCategory(categoryId, pageNumber, pageSize, sortBy, direction);
        return new ResponseEntity<>(allOfCategory,HttpStatus.OK);
    }
    @PutMapping("/products/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDto> updateProductWithCategory(@PathVariable String productId,@PathVariable String categoryId){
        ProductDto updateCategory = this.productService.updateCategory(categoryId, productId);
        return new ResponseEntity<>(updateCategory,HttpStatus.OK);

    }
}
