package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.OrderDto;

public interface OrderService {
	void createOrder(OrderDto orderDto);
	void updateOrderStatus(Long orderId, String status);
	void cancelOrder(Long orderId);
	List<OrderDto>findByStoreId(Long storeId);
	List<OrderDto>findByUserId(Long userId);
}
