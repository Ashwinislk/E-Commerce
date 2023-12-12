package com.electronicstore.service;

import com.electronicstore.payload.CategoryDto;
import com.electronicstore.payload.PageableResponse;

public interface CategoryService {

    public CategoryDto create(CategoryDto categoryDto);

    public CategoryDto updateCategory(CategoryDto categoryDto,String categoryid);

    PageableResponse<CategoryDto> getAll(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

    public CategoryDto getSingleCategory(String categoryid);

    void delete(String categoryid);
}
