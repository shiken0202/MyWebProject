package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Order;

public interface OrdersRepository  extends JpaRepository<Order, Long>{
	List<Order> findByStoreId(Long storeId);
	List<Order> findByUserId(Long userId);
	
}
