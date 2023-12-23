package com.electronicstore.service;

import com.electronicstore.payload.CreateOrderRequest;
import com.electronicstore.payload.OrderDto;
import com.electronicstore.payload.PageableResponse;

import java.util.List;

public interface OrderService {

    public OrderDto createOrder(CreateOrderRequest orderDto);

    public void removeOrder(String orderId);

    List<OrderDto> getOrdersOfUser(String userId);

    public PageableResponse<OrderDto> getOrders(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
}
