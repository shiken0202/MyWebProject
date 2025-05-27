package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.UserListDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class UserListController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/userlist")
	public ResponseEntity<ApiResponse<List<UserListDto>>> getUsers(){
		List<UserListDto>userListDtos=userService.findAllUsers();
		System.out.println(userListDtos);
		return ResponseEntity.ok(ApiResponse.success("使用者列表:", userListDtos));
	}
	
}
