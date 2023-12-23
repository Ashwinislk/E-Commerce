package com.electronicstore.repository;

import com.electronicstore.entity.Order;
import com.electronicstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByUser(User user);
}

