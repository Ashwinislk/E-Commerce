package com.electronicstore.service;

import com.electronicstore.entity.Product;
import com.electronicstore.entity.User;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.payload.ProductDto;
import com.electronicstore.payload.UserDto;
import com.electronicstore.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    Product product;

    Product product1;

    ProductDto productDto;

    @BeforeEach
    public void init(){
         product= Product.builder().title("Mi")
                .description("This contains mobile")
                .price(50000.00)
                .discountPrice(10000.00)
                .quantity(20)
                .stock(true)
                .live(false).build();

         product1= Product.builder().title("Vico")
                .description("This contains mobile")
                .price(50000.00)
                .discountPrice(10000.00)
                .quantity(20)
                .stock(true)
                .live(false).build();
    }
    @Test
    public void createProductTest(){
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto productDto = productService.saveProduct(modelMapper.map(product, ProductDto.class));

        Assertions.assertEquals("Mi",productDto.getTitle());
        Assertions.assertNotNull(productDto);
    }
    @Test
    public void getSingleProductTest(){
        String productId="123";
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        ProductDto productDto = productService.getSingleProduct(productId);
        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product.getTitle(),productDto.getTitle());
    }
    @Test
    public void updateProductTest(){
         productDto = ProductDto.builder().title("Mi")
                .description("This contains mobile")
                .price(50000.00)
                .discountPrice(10000.00)
                .quantity(20)
                .stock(true)
                .live(false).build();

         String productId="abc";

         Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
         Mockito.when(productRepository.save(product)).thenReturn(product);
        ProductDto dto = productService.updateProduct(productDto, productId);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("Mi",dto.getTitle());
    }
    @Test
    public void deleteProductTest(){
        String productId="abc";
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        productService.deleteProduct(productId);
        Mockito.verify(productRepository,Mockito.times(1)).delete(product);
    }
    @Test
    public void getAllProductsTest(){
        List<Product> products = Arrays.asList(product, product1);
        Page<Product> page=new PageImpl<>(products);
        Mockito.when(this.productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> allProducts = this.productService.getAllProducts(1, 10, "title", "desc");
        Assertions.assertNotNull(allProducts);

    }
    @Test
    public void getByliveTrueTest(){
        List<Product> products = Arrays.asList(product, product1);
        Page<Product> page=new PageImpl<>(products);

        Mockito.when(this.productRepository.findByLiveTrue((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> allProducts = this.productService.findByLiveTrue(1, 10, "title", "desc");
        Assertions.assertNotNull(allProducts);
        List<ProductDto> collect = allProducts.getContent().stream().filter(e -> e.getLive()==true).collect(Collectors.toList());
        Assertions.assertNotNull(collect);
    }


    @Test
    public void findProductsByTitleTest(){
        String keyword="abc";
        List<Product> products = Arrays.asList(product, product1);
        Page<Product> page=new PageImpl<>(products);
        Mockito.when(productRepository.findByTitleContaining((Pageable)Mockito.any(),Mockito.anyString())).thenReturn(page);
        PageableResponse<ProductDto> productsByTitle = this.productService.findProductsByTitle(10, 1, "title", "desc", keyword);
        Assertions.assertNotNull(productsByTitle);
        Assertions.assertEquals(2,productsByTitle.getContent().size());
    }


}
