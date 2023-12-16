package com.electronicstore.controller;

import com.electronicstore.entity.Category;
import com.electronicstore.payload.CategoryDto;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    Category category;

    @BeforeEach
    public void init(){
        category=Category.builder()
                .title("TV")
                .description("This is samsung TV")
                .coverImage("abc.png").build();
    }

    private String convertObjectToJsonString(Category category)  {
        try {
            return new ObjectMapper().writeValueAsString(category);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void createCategoryTest() throws Exception {

        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(this.categoryService.create(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());

    }

    @Test
    public void updateCategoryTest() throws Exception {

        String categoryId="abc";
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(this.categoryService.updateCategory(Mockito.any(),Mockito.anyString())).thenReturn(dto);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/categories/categoryid/"+categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void getSingleCategory() throws Exception {
        String categoryId="abc";
        CategoryDto dto = modelMapper.map(category, CategoryDto.class);
        Mockito.when(this.categoryService.getSingleCategory(Mockito.any())).thenReturn(dto);
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/categories/categoryid/"+categoryId)
                        .contentType(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());


    }
    @Test
    public void getAllCategories() throws Exception {

        CategoryDto dto = CategoryDto.builder().title("vivo").description("this is vivo phone").coverImage("abc.png").build();
        CategoryDto dto1 = CategoryDto.builder().title("Oppo").description("this is oppo phone").coverImage("xyz.png").build();
        CategoryDto dto2 = CategoryDto.builder().title("Mi").description("this is mi phone").coverImage("abc.png").build();

        PageableResponse<CategoryDto> pageResponse=new PageableResponse<>();
        pageResponse.setContent(Arrays.asList(dto,dto1,dto2));
        pageResponse.setLastPage(false);
        pageResponse.setPageNumber(100);
        pageResponse.setPageSize(10);
        pageResponse.setTotalElement(1000);

        Mockito.when(this.categoryService.getAll(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageResponse);
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/categories/allcategories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());


    }

    @Test
    public void deleteCategoryTest() throws Exception {

        String categoryId="abc";

        Mockito.doNothing().when(this.categoryService).delete(categoryId);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/categories/categoryid/"+categoryId)
        ).andDo(print()).andExpect(status().isOk());

    }


}
