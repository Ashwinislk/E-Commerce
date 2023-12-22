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

    private Integer price;

    private Integer quantity;

    private Date addedDate;

    private Boolean live;

    private Boolean stock;

    private Integer discountPrice;

    private String image;

    private CategoryDto category;
}
