package com.example.demo.controller;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/MyWebProject")
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
public class EmailConfirmController {
	
	@Autowired
	UserService userService;
	@GetMapping(value = "/email/confirm")
	public ResponseEntity<ApiResponse<Void>>confirm(@RequestParam String username,HttpSession session){
	try {
		userService.emailConfirmOK(username);
		
		session.invalidate();
		System.out.print(username);
		
		return ResponseEntity.ok(ApiResponse.success("驗證成功", null));
		
	}catch (UserNotFoundException e) {
		return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
	}
			
	}
	
}
