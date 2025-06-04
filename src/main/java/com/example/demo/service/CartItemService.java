package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.CartItemDto;
public interface CartItemService {
	List<CartItemDto>findAllCartItemsByUserId(Long userId); 
	void addItem(Long userId,Long productId ,Integer amount);
	void updateItem(Long cartItemId,Integer amount);
	void deleteItem(Long id);
}
