package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.CartItemNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.model.dto.CartItemDto;
import com.example.demo.model.entity.CartItem;
import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartItemService;
@Service
public class CartItemServiceImpl implements CartItemService{
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartItemMapper cartItemMapper;
	
	@Transactional(readOnly = true)
	@Override
	public List<CartItemDto> findAllCartItemsByUserId(Long userId) {
		Optional<User> userOpt=userRepository.findById(userId);
		if(userOpt.isEmpty()) {
			throw new UserNotFoundException("找不到使用者");
		}
		User user=userOpt.get();
		List<CartItem>cartItems=user.getCartItems();
		
		return cartItems.stream().map(c->cartItemMapper.toDto(c)).collect(Collectors.toList());
	}

	@Override
	public void addItem(Long userId,Long productId,Integer amount) {
		Optional<Product>productOpt=productRepository.findById(productId);
		Optional<User> userOpt=userRepository.findById(userId);
		if(userOpt.isEmpty()) {
			throw new UserNotFoundException("找不到使用者");
		}
		if(productOpt.isEmpty()) {
			throw new ProductNotFoundException("找不到商品");
		}
		CartItem cartItem=new CartItem();
		cartItem.setUser(userOpt.get());
		cartItem.setProduct(productOpt.get());
		cartItem.setQuantity(amount);
		cartItemRepository.save(cartItem);
	}

	@Override
	public void updateItem(Long id,Integer amount) {
		Optional<CartItem>cartItemOpt=cartItemRepository.findById(id);
		if(cartItemOpt.isEmpty()) {
			throw new CartItemNotFoundException("找不到該商品");
		}
		CartItem cartItem=cartItemOpt.get();
		cartItem.setId(id);
		cartItem.setQuantity(amount);
		cartItemRepository.save(cartItem);
	}

	@Override
	public void deleteItem(Long id) {
		Optional<CartItem>cartItemOpt=cartItemRepository.findById(id);
		if(cartItemOpt.isEmpty()) {
			throw new CartItemNotFoundException("找不到該商品");
		}
		cartItemRepository.delete(cartItemOpt.get());
		
	}

}
