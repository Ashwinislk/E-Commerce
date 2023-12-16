package com.electronicstore.service;

import com.electronicstore.entity.Category;
import com.electronicstore.payload.CategoryDto;
import com.electronicstore.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    Category category;

    Category category1;

    @BeforeEach
    public void init() {
        category = Category.builder().title("Mi")
                .description("This contains mobiles")
                .coverImage("abc.png").build();

        category1 = Category.builder().title("Apple")
                .description("This contains mobiles")
                .coverImage("abc.png").build();

    }

    @Test
    public void createCategoryTest() {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        CategoryDto categoryDto = this.categoryService.create(modelMapper.map(category, CategoryDto.class));
        Assertions.assertEquals("Mi", categoryDto.getTitle());
        Assertions.assertNotNull(categoryDto);
    }
    @Test
    public void updateCtegoryTest(){
        CategoryDto categoryDto = CategoryDto.builder().title("Mi")
                .description("This contains mobiles")
                .coverImage("abc.png").build();
        Mockito.when(categoryRepository.findById("categoryId")).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        CategoryDto categoryDto1 = this.categoryService.updateCategory(categoryDto, "categoryId");
        Assertions.assertNotNull(categoryDto1);
        Assertions.assertEquals("Mi",categoryDto1.getTitle());
    }
    @Test
    public void getSingleCategoryTest(){
        String caegoryId="123";
        Mockito.when(categoryRepository.findById(caegoryId)).thenReturn(Optional.of(category));
        CategoryDto categoryDto = this.categoryService.getSingleCategory(caegoryId);
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle());
    }
   @Test
    public void getAllCategoryTest(){
       Page page=new PageImpl(Arrays.asList(category,category1));
    Mockito.when(categoryRepository.findAll((Pageable)Mockito.any())).thenReturn(page);
       Sort.by("title").ascending();

    }
    @Test
    public void deleteCategoryTest(){
        String categotyId="123";
        Mockito.when(categoryRepository.findById(categotyId)).thenReturn(Optional.of(category));
        this.categoryService.delete(categotyId);
        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
    }

}
