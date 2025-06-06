package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.OrderItemService;
@Service
public class OrderItemServiceImpl implements OrderItemService{
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Override
	public List<OrderItemDto> findAllOrderItem(Long id) {
		List<OrderItem>orderItems=orderItemRepository.findAllByOrderId(id);
		return orderItems.stream().map(orderItemMapper::toDto).collect(Collectors.toList());
	}

}
