package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.UserException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserCertService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class LoginRestController {
	
	@Autowired
	UserCertService userCertService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EmailService emailService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Void>>Login(@RequestParam String username,@RequestParam String password,@RequestParam String captchaInput,HttpSession session){
		try {
			UserCert userCert=userCertService.getCert(username, password);
			session.setAttribute("userCert", userCert);
			String authcode=(String)session.getAttribute("authcode");
			System.out.println(captchaInput);
			System.out.println(authcode);
			if(!captchaInput.equals(authcode)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(ApiResponse.error(400,"驗證碼輸入失敗"));
			}
			return ResponseEntity.ok(ApiResponse.success("登入成功", null));
		} catch (UserException e) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(401, "登入失敗:"+e.getMessage()));
		}
	}
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse<Void>>Logout(HttpSession session){
		if(session.getAttribute("userCert")==null) {
			return ResponseEntity
					.status(HttpStatus
					.UNAUTHORIZED)
					.body(ApiResponse
							.error(401, "尚未登入，登出失敗"));
		}
		session.invalidate();
		return ResponseEntity.ok(ApiResponse.success("登出成功", null));
	}
	@GetMapping("/check-login")
	public ResponseEntity<ApiResponse<Boolean>> checkLogin(HttpSession session) {
	    boolean loggedIn = session.getAttribute("userCert") != null;
	    return ResponseEntity.ok(ApiResponse.success("檢查登入", loggedIn));
	}
	
	@GetMapping("/userinfo")
	public ResponseEntity<ApiResponse<UserCert>>userInfo(HttpSession session){
		UserCert userCert=(UserCert)session.getAttribute("userCert");
		if(userCert==null) {
			return null;
		}
		return ResponseEntity.ok(ApiResponse.success("使用者資訊為: ", userCert));
	}
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> Register(@RequestParam String username,@RequestParam String password,@RequestParam String email,@RequestParam String role){
		if(userService.existsByUserName(username)==true) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(400,"此用戶名已被使用: "+username));
			
		}
		userService.addUser(username, email, password, role);
		String emaillink="http://localhost:5173/MyWebProject/email/confirm?username="+username;
		emailService.sendEmail(email,emaillink);
		return ResponseEntity.ok(ApiResponse.success(username+", 註冊成功", null));
		
	}
	
	
}
