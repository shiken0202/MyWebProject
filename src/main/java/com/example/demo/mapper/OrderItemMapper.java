package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.model.entity.User;

@Component
public class OrderItemMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public OrderItemDto toDto(OrderItem orderItem) {
		// Entity 轉 DTO
		return modelMapper.map(orderItem, OrderItemDto.class);
	}
	
	public OrderItem toEntity(OrderItemDto orderItemDto) {
		// DTO 轉 Entity
		return modelMapper.map(orderItemDto, OrderItem.class);
	}
	
}
