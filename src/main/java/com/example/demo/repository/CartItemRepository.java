package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.CartItem;
import com.example.demo.model.entity.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	void deleteByUser(User user);

}
