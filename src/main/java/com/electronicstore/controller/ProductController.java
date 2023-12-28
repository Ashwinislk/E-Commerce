package com.electronicstore.controller;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.payload.ApiResponse;
import com.electronicstore.payload.ImageResponse;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.ProductDto;
import com.electronicstore.service.FileService;
import com.electronicstore.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${product.profile.image.path}")
    private String path;

    @Autowired
    private FileService fileService;


    /**
     * @author Ashwini Shelke
     * @apiNote save product records
     * @param productDto
     * @return
     * @since 1.0 v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        log.info("Entering the request for save product data");
        ProductDto productDto1 = this.productService.saveProduct(productDto);
        log.info("Completed the request for save product data");
        return new ResponseEntity<ProductDto>(productDto1, HttpStatus.CREATED);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote update product data
     * @param productDto
     * @param productId
     * @return
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/update/productId/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId) {
        log.info("Entering the request for update product data with productId:{}",productId);
        ProductDto productDto1 = this.productService.updateProduct(productDto, productId);
        log.info("Completed the request for update product data with ProductId:{}",productId);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get single product with id
     * @param productId
     * @return
     * @since 1.0v
     */
    @GetMapping("/products/productId/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {
        log.info("Entering the request for get single product with productId:{}",productId);
        ProductDto singleProduct = this.productService.getSingleProduct(productId);
        log.info("Completed the request for get single product with productId:{}",productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get all products data
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @since 1.0v
     */
    @GetMapping("/products")
    public ResponseEntity<PageableResponse> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
        log.info("Entering the request for get all product data");
        PageableResponse<ProductDto> allProducts = this.productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed the request for get all product data");
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote delete product with id
     * @param productId
     * @return
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/products/productId/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {
        log.info("Entering the request for delete single product data with productId:{}",productId);
        ApiResponse response = ApiResponse.builder().message(AppConstant.DELETE).status(false).httpcode(HttpStatus.OK).build();
        this.productService.deleteProduct(productId);
        log.info("Completed the request for delete single product data with productId:{}",productId);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get by live true
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @since 1.0v
     */
    @GetMapping("/products/livetrue")
    public ResponseEntity<PageableResponse> getByLiveTrue(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
        log.info("Entering the request for get by live true with pagination");
        PageableResponse<ProductDto> byLiveTrue = this.productService.findByLiveTrue(pageNumber, pageSize, sortBy, sortDir);
        log.info("Entering the request for get by live true with pagination");
        return new ResponseEntity<>(byLiveTrue, HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get all live products
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @since 1.0v
     */
    @GetMapping("/products/getAllLiveProducts")
    public ResponseEntity<PageableResponse> getAllLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {

        log.info("Entering the request for get all live products with pagination");
        PageableResponse<ProductDto> allLiveProducts = this.productService.getAllLiveProducts(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed the request for get all live products with pagination");
        return new ResponseEntity<>(allLiveProducts, HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get by product bt title
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @param keyword
     * @return
     * @since 1.0v
     */
    @GetMapping("/products/productsByTitle/{keyword}")
    public ResponseEntity<PageableResponse> getByProductsByTitle(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir, @PathVariable String keyword) {
        log.info("Entering the request for get by products by title with pagination");
        PageableResponse<ProductDto> productsByTitle = this.productService.findProductsByTitle(pageNumber,pageSize,sortBy, sortDir,keyword);
        log.info("Completed the request for get by products by title with pagination");
        return new ResponseEntity<>(productsByTitle, HttpStatus.OK);

    }

    /**
     * @author Ashwini Shelke
     * @apiNote save product with category
     * @param categoryId
     * @param productDto
     * @return
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
   @PostMapping("/category/{categoryId}")
    public ResponseEntity<ProductDto> saveProductWithCategoryId(@PathVariable String categoryId,@RequestBody ProductDto productDto){
       log.info("Entering the request for save products with category with categoryId:{}",categoryId);
        ProductDto product = this.productService.createProductWithCategory(productDto, categoryId);
       log.info("Completed the request for save products with category with categoryId:{}",categoryId);
        return new ResponseEntity<>(product,HttpStatus.CREATED);

    }

    /**
     * @author Ashwini Shelke
     * @apiNote  get all users category
     * @param categoryId
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param direction
     * @return
     * @since 1.0v
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductsWithCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCTS_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "direction", defaultValue = AppConstant.SORT_DIR, required = false) String direction   ){
        log.info("Entering the request for get all products category with categoryId:{}",categoryId);
        PageableResponse<ProductDto> allOfCategory = this.productService.getAllOfCategory(categoryId, pageNumber, pageSize, sortBy, direction);
        log.info("Completed the request for get all products category with categoryId:{}",categoryId);
        return new ResponseEntity<>(allOfCategory,HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote update product with category
     * @param productId
     * @param categoryId
     * @return
     * @since 1.0v
     */
    @PutMapping("/products/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDto> updateProductWithCategory(@PathVariable String productId,@PathVariable String categoryId){
        log.info("Entering the request for update product with category with categoryId and productId:{}:{}",categoryId,productId);
        ProductDto updateCategory = this.productService.updateCategory(categoryId, productId);
        log.info("Completed the request for update product with category with categoryId and productId:{}:{}",categoryId,productId);
        return new ResponseEntity<>(updateCategory,HttpStatus.OK);

    }

    /**
     * @author Ashwini Shelke
     * @apiNote upload product image
     * @param image
     * @param productId
     * @return
     * @throws IOException
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam MultipartFile image, @PathVariable String productId) throws IOException {

        log.info("Enter the  request for upload product image with productId:{}",productId);
        String file = this.fileService.uploadFile(image, path);

        ProductDto product = this.productService.getSingleProduct(productId);

        product.setImage(file);

        ProductDto updatedProduct = this.productService.updateProduct(product, productId);

        ImageResponse imageResponse = ImageResponse.builder().message("Image Uploaded").imageName(file).status(true).httpStatus(HttpStatus.CREATED).build();
        log.info("Completed the  request for upload product image with productId:{}",productId);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    /**
     * @author Ashwini Shelke
     * @apiNote get product Image
     * @param productId
     * @param response
     * @throws IOException
     */
    @GetMapping("/image/{ProductId}")
    public void getProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        log.info("Enter the  request for get product image with productId:{}",productId);
        ProductDto product = this.productService.getSingleProduct(productId);
        InputStream resource = this.fileService.getResource(path, product.getImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Completed the  request for get product image with productId:{}",productId);

    }
}
