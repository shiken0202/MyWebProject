package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.dto.StoreDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Store;
import com.example.demo.response.ApiResponse;
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
}
