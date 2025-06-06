package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.OrderItemDto;
import com.example.demo.model.dto.StoreDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Store;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.OrderItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.StoreService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping("/order/create")
	public ResponseEntity<ApiResponse<Void>> checkout(HttpSession session,@RequestBody List<OrderDto> orderDtos){
		
		UserCert userCert=(UserCert)session.getAttribute("userCert");
		if(userCert==null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, "尚未登入"));
		}

		for (OrderDto dto : orderDtos) {
            if (!dto.getUserId().equals(userCert.getUserId())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400,"message:用戶 ID 不一致"));
            }
        }

        // 建立多筆訂單
        orderDtos.forEach(orderService::createOrder);
		
		return ResponseEntity.ok(ApiResponse.success("訂單建立成功", null));
	}
	@GetMapping("/orders/user")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByUser(HttpSession session){
		UserCert userCert=(UserCert)session.getAttribute("userCert");
		if(userCert==null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, "尚未登入"));
		}
		if(userCert.getRole()!="BUYER") {
			
			return ResponseEntity.ok(ApiResponse.error(405, "尚未登入買家身分"));
		}
		List <OrderDto>orderDtos= orderService.findByUserId(userCert.getUserId());

		return ResponseEntity.ok(ApiResponse.success("訂單建立成功", orderDtos));
		
	}
	@GetMapping("/orders/store")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByStore(HttpSession session){
		UserCert userCert=(UserCert)session.getAttribute("userCert");
		if(userCert==null) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, "尚未登入"));
		}
		if(userCert.getRole()!="SELLER") {
			
			return ResponseEntity.ok(ApiResponse.error(405, "尚未登入賣家身分"));
		}
		StoreDto storeDto=storeService.findStoreByUserId(userCert.getUserId());
		List <OrderDto>orderDtos= orderService.findByStoreId(storeDto.getId());

		return ResponseEntity.ok(ApiResponse.success("訂單建立成功", orderDtos));
		
	}
	@PutMapping("/orders/cancel/{id}")
	public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long id){
		try {
			orderService.cancelOrder(id);
		return ResponseEntity.ok(ApiResponse.success("取消成功", null));
		} catch (OrderNotFoundException e) {
			return ResponseEntity.ok(ApiResponse.error(400,"取消失敗"));
		}
	}
	@PutMapping("/orders/edit/{id}")
	public ResponseEntity<ApiResponse<Void>> editOrder(@PathVariable Long id,@RequestParam String status){
		try {
			orderService.updateOrderStatus(id,status);
		return ResponseEntity.ok(ApiResponse.success("編輯狀態成功", null));
		} catch (OrderNotFoundException e) {
			return ResponseEntity.ok(ApiResponse.error(400,"編輯狀態成功失敗"));
		}
	}
	@GetMapping("/orderitems/{id}")
	public ResponseEntity<ApiResponse<List<OrderItemDto>>> getAllOrderItems(@PathVariable("id") Long id){
		List<OrderItemDto>orderItemDtos=orderItemService.findAllOrderItem(id);
		return ResponseEntity.ok(ApiResponse.success("查詢細項成功", orderItemDtos));
			
		}
}
