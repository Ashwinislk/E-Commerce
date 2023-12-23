package com.electronicstore.payload;

import com.electronicstore.entity.Order;
import com.electronicstore.entity.Product;
import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

    private Integer orderItemId;

    private Integer quantity;

    private Integer totalPrice;

    private ProductDto product;



}
