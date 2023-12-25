package com.electronicstore.controller;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.payload.AddItemToCartRequest;
import com.electronicstore.payload.ApiResponse;
import com.electronicstore.payload.CartDto;
import com.electronicstore.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("carts/userId/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable String userId) {
        log.info("Entering the request for add item to cart with userId:{}",userId);
        CartDto cartDto = cartService.addItemToCart(userId, request);
        log.info("Completed the request for add item to cart with userId:{}",userId);
        return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/carts/userId/{userId}/itemId/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable String userId,@PathVariable Integer itemId) {
        log.info("Entering the request for remove item from cart with userId and itemId:{}:{}",userId,itemId);
        cartService.removeItemFromCart(userId, itemId);
        ApiResponse response = ApiResponse.builder().message(AppConstant.DELETE)
                .status(true)
                .httpcode(HttpStatus.OK)
                .build();
        log.info("Completed the request for remove item from cart with userId and itemId:{}:{}",userId,itemId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("carts/userId/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId) {
        log.info("Entering the request for clear cart with userId:{}",userId);
        cartService.clearCart(userId);
        ApiResponse response = ApiResponse.builder().message(AppConstant.DELETE)
                .status(true)
                .httpcode(HttpStatus.OK)
                .build();
        log.info("Completed the request for clear cart with userId:{}",userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("carts/userId/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        log.info("Entering the request for get cart with userId:{}",userId);
        CartDto cartByUser = cartService.getCartByUser(userId);
        log.info("Completed the request for get cart with userId:{}",userId);
        return new ResponseEntity<>(cartByUser, HttpStatus.OK);
    }
}
