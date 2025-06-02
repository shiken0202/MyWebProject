package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.model.dto.StoreDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.StoreService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class StoreController {
	@Autowired
	public StoreService storeService;
	
	@PostMapping("/store/create")
	public ResponseEntity<ApiResponse<Void>> addStore(@RequestBody StoreDto storeDto){
		storeService.addStore(storeDto.getStoreName(), storeDto.getDescription(), storeDto.getUserId());
		return ResponseEntity.ok(ApiResponse.success("新增商店成功", null));
	}
	@GetMapping("/store/info")
	public ResponseEntity<ApiResponse<StoreDto>>getStore(HttpSession session){
		UserCert userCert=(UserCert) session.getAttribute("userCert");
		StoreDto storeDto =storeService.findStoreByUserId(userCert.getUserId());
		session.setAttribute("storeInfo", storeDto);
		if(storeDto==null) {
			return ResponseEntity.ok(ApiResponse.success("尚無商店，請建立商店", null));
		}
		return ResponseEntity.ok(ApiResponse.success("查詢商店成功", storeDto));
	}
	@PutMapping("/store/update")
	public ResponseEntity<ApiResponse<Void>>updateStoreDescription(@RequestBody StoreDto storeDto,HttpSession session){
		
		StoreDto storeInfo=(StoreDto) session.getAttribute("storeInfo");
		storeService.updateStoreDescription(storeDto.getDescription(), storeInfo.getId());
		System.out.println(storeInfo+"這是info");
		System.out.println(storeDto+"這是description");
		if(storeInfo==null) {
			throw new StoreNotFoundException("查無商店無法修改");
		}
		return ResponseEntity.ok(ApiResponse.success("修改成功", null));
		
	}
	@GetMapping("/store/all")
	public ResponseEntity<ApiResponse<List<StoreDto>>> findAllStore(){
		List<StoreDto>stores=storeService.findAllStores();
		return ResponseEntity.ok(ApiResponse.success("商店列表", stores));
	}
}
