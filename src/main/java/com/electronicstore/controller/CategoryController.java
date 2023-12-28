package com.electronicstore.controller;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.payload.*;
import com.electronicstore.service.CategoryService;
import com.electronicstore.service.FileService;
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
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Value("${category.profile.image.path}")
    private String path;

    @Autowired
    private FileService fileService;

    /**
     * @author Ashwini Shelke
     * @apiNote create category record
     * @param categoryDto
     * @return
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        log.info("Entering the request for save category data");
        CategoryDto categoryDto1 = this.categoryService.create(categoryDto);
        log.info("Completed the request for save category data");
        return new ResponseEntity<CategoryDto>(categoryDto1, HttpStatus.CREATED);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote update category with id
     * @param categoryDto
     * @param categoryid
     * @return
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/categories/categoryid/{categoryid}")
    public ResponseEntity<CategoryDto> updatecategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable String categoryid){
        log.info("Entering the request for update category data with categoryid:{}",categoryid);
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryid);
        log.info("Completed the request for update category data with categoryid :{}",categoryid);
        return new ResponseEntity<CategoryDto>(updateCategory,HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get all category record
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @since 1.0v
     */
    @GetMapping("/categories/allcategories")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.CATEGORY_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
        log.info("Entering the request for get all category data");
        PageableResponse<CategoryDto> allCategories = this.categoryService.getAll(pageSize,pageNumber, sortBy, sortDir);
        log.info("Completed the request for get all category data");
        return new ResponseEntity<PageableResponse<CategoryDto>>(allCategories, HttpStatus.OK);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get single category with categoryid
     * @param categoryid
     * @return
     * @since 1.0v
     */
    @GetMapping("/categories/categoryid/{categoryid}")
    public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable String categoryid){
        log.info("Entering the request for get single category data with categoryid:{}",categoryid);
        CategoryDto singleCategory = this.categoryService.getSingleCategory(categoryid);
        log.info("Completed the request for get single category data with categoryid:{}",categoryid);
        return new ResponseEntity<CategoryDto>(singleCategory,HttpStatus.OK);

    }

    /**
     * @author Ashwini Shelke
     * @apiNote delete category data with categoryid
     * @param categoryid
     * @return
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/categories/categoryid/{categoryid}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String categoryid){
        log.info("Entering the request for delete category data with categoryid:{}",categoryid);
        ApiResponse api=new ApiResponse();
        api.setMessage(AppConstant.DELETE);
        api.setHttpcode(HttpStatus.OK);
        api.setStatus(true);
        this.categoryService.delete(categoryid);
        log.info("Completed the request for delete category data with categoryid:{}",categoryid);
        return new ResponseEntity<ApiResponse>(api,HttpStatus.OK);

    }

    /**
     * @author Ashwini Shelke
     * @apiNote upload category Image
     * @param image
     * @param categoryId
     * @return
     * @throws IOException
     * @throws IOException
     * @since 1.0v
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam MultipartFile image, @PathVariable String categoryId) throws IOException, IOException {
        log.info("Entering the request for upload image with categoryId:{}", categoryId);
        String imageName = this.fileService.uploadFile(image, path);
        CategoryDto category = this.categoryService.getSingleCategory(categoryId);
        category.setCoverImage(imageName);
        CategoryDto updateCategory = this.categoryService.updateCategory(category, categoryId);
        ImageResponse imageResponse = ImageResponse.builder().message("Image Upload Successfully").imageName(imageName).status(true).httpStatus(HttpStatus.CREATED).build();
        log.info("Completed the request for upload image with categoryId:{}", categoryId);
        return new ResponseEntity<ImageResponse>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * @author Ashwini Shelke
     * @apiNote get category image
     * @param categoryId
     * @param response
     * @throws IOException
     * @since 1.0v
     */
    @GetMapping("/categories/image/{categoryId}")
    public void getCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        log.info("Enter the request for Get Image with categoryId : {}", categoryId);
        CategoryDto singleCategory = categoryService.getSingleCategory(categoryId);
        log.info("CategoryImage Name : {}", singleCategory.getCoverImage());
        InputStream resource = fileService.getResource(path, singleCategory.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        log.info("Completed the request for Get Image with categoryId : {}", categoryId);
        StreamUtils.copy(resource, response.getOutputStream());

    }


}
