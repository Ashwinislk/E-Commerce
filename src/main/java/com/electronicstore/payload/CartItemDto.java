package com.electronicstore.payload;

import com.electronicstore.entity.Product;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Integer cartItemId;

    private Integer quantity;

    private Integer totalPrice;

    private ProductDto product;


}
