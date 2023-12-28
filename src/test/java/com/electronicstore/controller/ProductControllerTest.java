package com.electronicstore.controller;

import com.electronicstore.entity.Product;
import com.electronicstore.entity.User;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.ProductDto;
import com.electronicstore.payload.UserDto;
import com.electronicstore.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    Product product;

    @BeforeEach
    public void init() {
        product = Product.builder().title("Mi")
                .description("This contains mobile")
                .price(50000)
                .discountPrice(10000)
                .quantity(20)
                .stock(true)
                .live(false).build();
    }

    @Test
    public void createProductTest() throws Exception {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.saveProduct(Mockito.any())).thenReturn(productDto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products/").contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());


    }

    private String convertObjectToJsonString(Product product) {
        try {
            return new ObjectMapper().writeValueAsString(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    @Test
    public void updateProducttTest() throws Exception {
        String productId = "123";
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.updateProduct(Mockito.any(), Mockito.anyString())).thenReturn(productDto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/products/update/productId/" + productId).contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(product))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void getSingleProductTest() throws Exception {
        String productId = "123";
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        Mockito.when(productService.getSingleProduct(productId)).thenReturn(productDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/productId/" + productId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAllProductsTest() throws Exception {
        ProductDto productDto = ProductDto.builder().title("Mi")
                .description("This contains mobile")
                .price(50000)
                .discountPrice(10000)
                .quantity(20)
                .stock(true)
                .live(false).build();

        ProductDto productDto1 = ProductDto.builder().title("Vivo")
                .description("This contains mobile")
                .price(60000)
                .discountPrice(15000)
                .quantity(22)
                .stock(true)
                .live(false).build();

        PageableResponse<ProductDto> pages = new PageableResponse<>();

        pages.setContent(Arrays.asList(productDto, productDto1));
        pages.setLastPage(false);
        pages.setPageNumber(100);
        pages.setPageSize(10);
        pages.setTotalElement(1000);
        Mockito.when(productService.getAllProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pages);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProductTest() throws Exception {
        String productId = "123";
        Mockito.doNothing().when(productService).deleteProduct(productId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/productId/" + productId))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getByLiveTrueTest() throws Exception {
        ProductDto productDto = ProductDto.builder().title("Mi")
                .description("This contains mobile")
                .price(50000)
                .discountPrice(10000)
                .quantity(20)
                .stock(true)
                .live(false).build();

        ProductDto productDto1 = ProductDto.builder().title("Vivo")
                .description("This contains mobile")
                .price(60000)
                .discountPrice(15000)
                .quantity(22)
                .stock(true)
                .live(false).build();

        PageableResponse<ProductDto> pages = new PageableResponse<>();

        pages.setContent(Arrays.asList(productDto, productDto1));
        pages.setLastPage(false);
        pages.setPageNumber(100);
        pages.setPageSize(10);
        pages.setTotalElement(1000);
        Mockito.when(productService.findByLiveTrue(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pages);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products/livetrue/").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getProductByTitleTest() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .title("Apple")
                .description("This Launched in 2023")
                .price(50000)
                .discountPrice(45000)
                .quantity(15)
                .live(true)
                .stock(true).build();

        ProductDto productDto1 = ProductDto.builder()
                .title("Apple")
                .description("This Launched in 2022")
                .price(100000)
                .discountPrice(95000).quantity(5)
                .live(true).stock(true).build();

        PageableResponse<ProductDto> pages= new PageableResponse<>();
        pages.setContent(Arrays.asList(productDto, productDto1));
        pages.setLastPage(false);
        pages.setPageNumber(100);
        pages.setPageSize(10);
        pages.setTotalElement(1000);

        String keyword = "App";
        Mockito.when(this.productService.findProductsByTitle(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString())).thenReturn(pages);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/productsByTitle/" + keyword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());


    }
}