package com.electronicstore.controller;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.payload.ApiResponse;
import com.electronicstore.payload.CategoryDto;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.UserDto;
import com.electronicstore.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @author Ashwini Shelke
     * @apiNote create category record
     * @param categoryDto
     * @return
     * @since 1.0v
     */
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


}
