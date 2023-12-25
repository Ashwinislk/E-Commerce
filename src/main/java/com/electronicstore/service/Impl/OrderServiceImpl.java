package com.electronicstore.service.Impl;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.entity.*;
import com.electronicstore.exception.BadApiRequest;
import com.electronicstore.exception.ResourceNotFound;
import com.electronicstore.payload.CreateOrderRequest;
import com.electronicstore.payload.Helper;
import com.electronicstore.payload.OrderDto;
import com.electronicstore.payload.PageableResponse;
import com.electronicstore.repository.CartRepository;
import com.electronicstore.repository.OrderRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        log.info("Entering the dao call for create order");
        {

            // Create UserId & CartId From oderDto
            String userId = orderDto.getUserId();
            String cartId = orderDto.getCartId();
            //find User
            User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND));
            // Find Cart
            Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND));

            List<CartItem> cartItems = cart.getItems();

            if (cartItems.size() == 0) {
                throw new BadApiRequest(AppConstant.NOT_VALID_QUANTITY);
            }
            // generate Order

            Order order = Order.builder()
                    .billingName(orderDto.getBillingName())
                    .billingPhone(orderDto.getBillingPhone())
                    .billingAddress(orderDto.getBillingAddress())
                    .orderDate(new Date())
                    .deliveredDate(null)
                    .paymentStatus(orderDto.getPaymentStatus())
                    .orderStatus(orderDto.getOrderStatus())
                    .orderId(UUID.randomUUID().toString())
                    .user(user)
                    .build();

            AtomicReference<Integer> orderAmount=new AtomicReference<>(0);

            //Convert cartItem to OrderItem
            List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

                OrderItem orderItem = OrderItem.builder()
                        .quantity(cartItem.getQuantity())
                        .product(cartItem.getProduct())
                        .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountPrice())
                        .order(order)
                        .build();
                orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
                return orderItem;

            }).collect(Collectors.toList());

            order.setOrederitems(orderItems);
            order.setOrderAmount(orderAmount.get());

            cart.getItems().clear();
            cartRepository.save(cart);
            Order saveOrder = orderRepository.save(order);
            log.info("Completed the dao call for create order");
            return this.modelMapper.map(saveOrder, OrderDto.class);
        }
    }

    @Override
    public void removeOrder(String orderId) {
        log.info("Entering the dao call for remove order with orderId:{}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + orderId));
        orderRepository.delete(order);
        log.info("Completed the dao call for remove order with orderId:{}",orderId);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        log.info("Entering the dao call for get orders of users with userId:{}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + userId));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        log.info("Completed the dao call for get orders of users with userId:{}",userId);
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Entering the dao call for get orders");
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        PageRequest pageRequest = PageRequest.of(pageSize, pageNumber, sort);
        Page<Order> page = orderRepository.findAll(pageRequest);
        log.info("Completed the dao call for get orders");
        return Helper.getPageableResponse(page,OrderDto.class);
    }
}
