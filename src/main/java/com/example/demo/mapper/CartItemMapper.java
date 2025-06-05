package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.CartItemDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.CartItem;
import com.example.demo.model.entity.User;

@Component
public class CartItemMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CartItemDto toDto(CartItem cartItem) {
		CartItemDto cartItemDto=modelMapper.map(cartItem, CartItemDto.class);
		cartItemDto.setProductName(cartItem.getProduct().getTitle());
		cartItemDto.setBrand(cartItem.getProduct().getBrand());
		cartItemDto.setPrice(cartItem.getProduct().getPrice());
		cartItemDto.setStock(cartItem.getProduct().getStock());
		cartItemDto.setCategoryName(cartItem.getProduct().getCategory().getName());
		cartItemDto.setCategoryId(cartItem.getProduct().getCategory().getId());
		cartItemDto.setStoreId(cartItem.getProduct().getStore().getId());
		return cartItemDto ;
	}
	
	public CartItem toEntity(CartItemDto cartItemDto) {
		// DTO 轉 Entity
		return modelMapper.map(cartItemDto, CartItem.class);
	}
	
}
