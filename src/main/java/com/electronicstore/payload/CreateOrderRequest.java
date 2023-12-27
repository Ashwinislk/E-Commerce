package com.electronicstore.payload;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private String userId;

    private String cartId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOTPAID";

    private String billingAddress;

    private String billingPhone;

    private String billingName;


}
