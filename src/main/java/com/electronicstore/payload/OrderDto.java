package com.electronicstore.payload;

import com.electronicstore.entity.OrderItem;
import com.electronicstore.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {


    private String orderId;

    private String orderStatus="PENDING";

    private String paymentStatus="NOTPAID";

    private Integer orderAmount;

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderDate;

    private Date deliveredDate;


    private UserDto user;


    private List<OrderItemDto> orederitems=new ArrayList<>();
}
