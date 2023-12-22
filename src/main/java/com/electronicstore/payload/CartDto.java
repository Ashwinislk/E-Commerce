package com.electronicstore.payload;

import com.electronicstore.entity.CartItem;
import com.electronicstore.entity.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private String cartId;

    private Date createdAt;

    private UserDto user;

    private List<CartItemDto> items=new ArrayList<>();
}
