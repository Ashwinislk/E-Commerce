package com.electronicstore.payload;

import lombok.*;

import javax.persistence.Entity;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String  productId;

    private String title;

    private String description;

    private Double price;

    private Integer quantity;

    private Date addedDate;

    private Boolean live;

    private Boolean stock;

    private Double discountPrice;

    private CategoryDto category;
}
