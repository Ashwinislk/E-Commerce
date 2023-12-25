package com.electronicstore.service.Impl;

import com.electronicstore.constants.AppConstant;
import com.electronicstore.entity.Cart;
import com.electronicstore.entity.CartItem;
import com.electronicstore.entity.Product;
import com.electronicstore.entity.User;
import com.electronicstore.exception.BadApiRequest;
import com.electronicstore.exception.ResourceNotFound;
import com.electronicstore.payload.AddItemToCartRequest;
import com.electronicstore.payload.CartDto;
import com.electronicstore.repository.CartItemRepository;
import com.electronicstore.repository.CartRepository;
import com.electronicstore.repository.ProductRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        log.info("Entering the dao call for add item to cart with userId:{}",userId);
        {

            Integer quantity = request.getQuantity();
            String productId = request.getProductId();

            if (quantity <= 0) {
                throw new BadApiRequest(AppConstant.NOT_VALID_QUANTITY);

            }

            //Find A Product
            Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + productId));
            // Find User From Cart
            User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + userId));

            // We Find Cart From User
            Cart cart = null;
            try {
                cart = cartRepository.findByUser(user).get();
            } catch (NoSuchElementException ex) {
                // If User Has No Cart
                cart = new Cart();
                cart.setCartId(UUID.randomUUID().toString());

                cart.setCreatedAt(new Date());
            }

            // Perform Cart Operation
            // If Cart item Already Present then update
            List<CartItem> items = cart.getItems();

            AtomicReference<Boolean> updated = new AtomicReference<>(false);

            items = items.stream().map(item -> {

                if (item.getProduct().getProductId().equals(productId)) {
                    //Iteam Already Present in Cart
                    item.setQuantity(quantity);
                    item.setTotalPrice(quantity * product.getPrice());
                    updated.set(true);
                }

                return item;
            }).collect(Collectors.toList());

//        cart.setItems(updatedList);


            if (!updated.get()) {
                CartItem cartItem = CartItem.builder()
                        .quantity(quantity)
                        .totalPrice(quantity * product.getPrice())
                        .cart(cart)
                        .product(product)
                        .build();
                cart.getItems().add(cartItem);
            }


            cart.setUser(user);

            Cart updatedCart = cartRepository.save(cart);
            log.info("Completed the dao call for add item to cart with userId:{}",userId);
            return this.modelMapper.map(updatedCart, CartDto.class);
        }
    }

    @Override
    public void removeItemFromCart(String userId, Integer cartItem) {
        log.info("Entering the dao call for remove item from cart with userId and cartItem:{}:{}",userId,cartItem);
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + cartItem));
        cartItemRepository.delete(cartItem1);
        log.info("Completed the dao call for add item to cart with userId and cartItem:{}:{}",userId,cartItem);
    }

    @Override
    public void clearCart(String userId) {
        log.info("Entering the dao call for clear cart with userId:{}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + userId));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND + userId));
        cart.getItems().clear();
        cartRepository.save(cart);
        log.info("Completed the dao call for clear cart with userId:{}",userId);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        log.info("Entering the dao call for get cart By user with userId:{}",userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND));
        Cart cart = this.cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFound(AppConstant.NOT_FOUND));
        log.info("Completed the dao call for get cart by user with userId:{}",userId);
        return modelMapper.map(cart, CartDto.class);


    }
}
