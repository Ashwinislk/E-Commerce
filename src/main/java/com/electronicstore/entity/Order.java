package com.electronicstore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Order {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId;

    private String orderStatus;

    private String paymentStatus;

    private Integer orderAmount;

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    private  User user;

    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderitems=new ArrayList<>();
}
