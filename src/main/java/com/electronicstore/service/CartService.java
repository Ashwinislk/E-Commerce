package com.electronicstore.service;

import com.electronicstore.payload.AddItemToCartRequest;
import com.electronicstore.payload.CartDto;

public interface CartService {

    public CartDto addItemToCart(String userId, AddItemToCartRequest request);

    public void removeItemFromCart(String userId,Integer cartItem);

    public void clearCart(String userId);

    public CartDto getCartByUser(String userId);
}
