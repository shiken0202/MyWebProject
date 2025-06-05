package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.CartItemDto;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CartItemService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class CartItemController {
	@Autowired
	private CartItemService cartItemService;
	
	@PostMapping("/cartitem/create")
	public ResponseEntity<ApiResponse<Void>> addItems(HttpSession session,ProductDto productDto,@RequestParam Integer amount){
		UserCert userCert=(UserCert)session.getAttribute("userCert");
		cartItemService.addItem(userCert.getUserId(), productDto.getId(), amount);
		return ResponseEntity.ok(ApiResponse.success("商品新增成功", null));
	}
	@GetMapping("/cartitem/allitems")
	public ResponseEntity<ApiResponse<List<CartItemDto>>> findAllItemsByUserId(HttpSession session){
		UserCert userCert=(UserCert) session.getAttribute("userCert");
		List<CartItemDto>cartItemDtos= cartItemService.findAllCartItemsByUserId(userCert.getUserId());
		return ResponseEntity.ok(ApiResponse.success("購物車商品查詢成功", cartItemDtos));
	}
	@PutMapping("/cartitem/update")
	public ResponseEntity<ApiResponse<Void>>updateItem(@RequestBody CartItemDto cartItemDto){
		cartItemService.updateItem(cartItemDto.getId(), cartItemDto.getQuantity());
		return ResponseEntity.ok(ApiResponse.success("購物車商品查詢成功", null));
	}
	@DeleteMapping("/cartitem/delete/{id}")
	public ResponseEntity<ApiResponse<Void>>deleteItem(@PathVariable("id") Long cartItemId){
		cartItemService.deleteItem(cartItemId);
		return ResponseEntity.ok(ApiResponse.success("購物車商品刪除成功", null));
	}
}
