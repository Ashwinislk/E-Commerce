package com.electronicstore.service.Impl;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.entity.Category;
import com.electronicstore.exception.ResourceNotFound;
import com.electronicstore.payload.CategoryDto;
import com.electronicstore.payload.Helper;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.repository.CategoryRepository;
import com.electronicstore.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        log.info("Entering the dao call for save category data");
        String str = UUID.randomUUID().toString();
        categoryDto.setCategoryId(str);
        Category category = this.modelMapper.map(categoryDto, Category.class);
        this.categoryRepository.save(category);
        CategoryDto categoryDto1 = this.modelMapper.map(category, CategoryDto.class);
        log.info("Completed the dao call for create category data");
        return categoryDto1;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryid) {
        log.info("Entering the dao call for update category data");
        Category category = categoryRepository.findById(categoryid).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + categoryid));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updateCategory = this.categoryRepository.save(category);
        CategoryDto categoryDto1 = this.modelMapper.map(updateCategory, CategoryDto.class);
        log.info("Completed the dao call for update category data");
        return categoryDto1;
    }

    @Override
    public PageableResponse<CategoryDto> getAll(Integer pageNumber, Integer pageSize, String sortBy,String sortDir) {
        log.info("Entering the dao call for get all category data");
        Sort desc=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        PageRequest pageRequest = PageRequest.of(pageSize, pageNumber, desc);
        Page<Category> categories = this.categoryRepository.findAll(pageRequest);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(categories, CategoryDto.class);
        log.info("Completed the dao call for get all category data");
        return pageableResponse;
    }

    @Override
    public CategoryDto getSingleCategory(String categoryid) {
        log.info("Entering the dao call for get single category data :{}",categoryid);
        Category category = this.categoryRepository.findById(categoryid).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + categoryid));
        CategoryDto categoryDto = this.modelMapper.map(category, CategoryDto.class);
        log.info("Completed the dao call for get single category data :{}",categoryid);
        return categoryDto;
    }

    @Override
    public void delete(String categoryid) {
        log.info("Entering the dao call for delete category data :{}",categoryid);
        Category category = this.categoryRepository.findById(categoryid).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + categoryid));
        this.categoryRepository.delete(category);
        log.info("Completed the dao call for delete category data :{}",categoryid);
    }
}
