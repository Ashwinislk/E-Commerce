package com.electronicstore.controller;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.payload.ApiResponse;
import com.electronicstore.payload.CreateOrderRequest;
import com.electronicstore.payload.OrderDto;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        log.info("Entering the request for create order with order data");
        OrderDto order = orderService.createOrder(request);
        log.info("Completed the request for create order with order data");
        return new ResponseEntity<OrderDto>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/orders/orderId/{orderId}")
    public ResponseEntity<ApiResponse> removeOrder(@PathVariable String orderId) {
        log.info("Entering the request for remove order with orderId:{}",orderId);
        orderService.removeOrder(orderId);
        ApiResponse response = ApiResponse.builder().message(AppConstant.DELETE)
                .status(true)
                .httpcode(HttpStatus.OK)
                .build();
        log.info("Completed the request for remove order with orderId:{}",orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/orders/user/userId/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
        log.info("Entering the request for get orders of users with userId:{}",userId);
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        log.info("Completed the request for get orders of users with userId:{}",userId);
        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
    }

    @GetMapping("/orders/allorders")
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.ORDER_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir

    ) {
        log.info("Entering the request for get orders with pagination");
        PageableResponse<OrderDto> orders = orderService.getOrders(pageSize,pageNumber, sortBy, sortDir);
        log.info("Completed the request for get orders with pagination");
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
