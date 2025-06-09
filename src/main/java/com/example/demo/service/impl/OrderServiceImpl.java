package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.dto.CartItemDto;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.OrderItem;
import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrdersRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
@Service
@Transactional
public class OrderServiceImpl  implements OrderService{
	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	public void createOrder(OrderDto orderDto) {
		User user=userRepository.findById(orderDto.getUserId()).orElseThrow(()->new UserNotFoundException("找不到用戶"));
		Store store=storeRepository.findById(orderDto.getStoreId()).orElseThrow(()->new StoreNotFoundException("找不到商家"));
		Order order =new Order();
		order.setUser(user);
		order.setStore(store);
		order.setAddress(orderDto.getAddress());
		order.setPaymentType(orderDto.getPaymentType());
		order.setDeliveryMethod(orderDto.getDeliveryMethod());
		order.setStatus("待出貨");
		
		 List<OrderItem> orderItems = new ArrayList<>();
	        BigDecimal totalAmount = BigDecimal.ZERO;
		
	        for(OrderItemDto orderItemDto:orderDto.getOrderItems()) {
	        	Product product = productRepository.findById(orderItemDto.getProductId())
	                    .orElseThrow(() -> new ProductNotFoundException("商品不存在 ID: " + orderItemDto.getProductId()));
	        	if (product.getStock() < orderItemDto.getQuantity()) {
	                throw new RuntimeException("商品庫存不足: " + product.getTitle());
	            }
	        	 OrderItem orderItem = new OrderItem();
	        	 orderItem.setOrder(order);
	        	 orderItem.setProduct(product);
	        	 orderItem.setQuantity(orderItemDto.getQuantity());
	        	 orderItem.setPrice(product.getPrice());
	        	 orderItem.setProductTitleSnapshot(product.getTitle());
	        	 orderItem.setProductBrandSnapshot(product.getBrand());
	        	 orderItem.setProductPriceSnapshot(product.getPrice());
	        	 orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(orderItemDto.getQuantity())));
	        	 totalAmount=totalAmount.add(orderItem.getSubtotal());
	        	 product.setStock(product.getStock()-orderItemDto.getQuantity());
	        	 productRepository.save(product);
	        	 orderItems.add(orderItem);
			}
	        order.setTotalAmount(totalAmount);
	        order.setOrderItems(orderItems);
	        ordersRepository.save(order);
	        cartItemRepository.deleteByUser(user);
	}

	@Override
	public void updateOrderStatus(Long orderId, String status) {
		 Order order = ordersRepository.findById(orderId)
		            .orElseThrow(() -> new OrderNotFoundException("訂單不存在"));
		    order.setStatus(status);
		    ordersRepository.save(order);
	}

	@Override
	public void cancelOrder(Long orderId) {
		Order order =ordersRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("訂單不存在"));
		order.getOrderItems().forEach(item->{
			Product product=item.getProduct();
			product.setStock(product.getStock()+item.getQuantity());
			productRepository.save(product);
		});;
		order.setStatus("已取消");
		ordersRepository.save(order);
	}

	@Override
	public List<OrderDto> findByStoreId(Long storeId) {
		List<Order> orders=ordersRepository.findByStoreId(storeId);
		
		return orders.stream().map((o)->orderMapper.toDto(o)).collect(Collectors.toList());
	}

	@Override
	public List<OrderDto> findByUserId(Long userId) {
		List<Order> orders=ordersRepository.findByUserId(userId);
		return orders.stream().map((o)->orderMapper.toDto(o)).collect(Collectors.toList());
	}

}
