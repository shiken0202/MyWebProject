package com.example.demo.controller;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.LoginResponse;
import com.example.demo.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	RedisTemplate<String,String> redisTemplate;

//	@PostMapping("/login")
//	public ResponseEntity<ApiResponse<Void>>Login(@RequestParam String username,@RequestParam String password,@RequestParam String captchaInput,HttpSession session){
//		try {
//			UserCert userCert=userCertService.getCert(username, password);
//			session.setAttribute("userCert", userCert);
//			String authcode=(String)session.getAttribute("authcode");
//
//			if(!captchaInput.equals(authcode)) {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//						.body(ApiResponse.error(400,"驗證碼輸入失敗"));
//			}
//			if(userCert.isIsbanned()==true){
//				session.invalidate();
//				return ResponseEntity.ok(ApiResponse.success("你已被封鎖", null));
//			}
//			return ResponseEntity.ok(ApiResponse.success("登入成功", null));
//		} catch (UserException e) {
//			return ResponseEntity
//					.status(HttpStatus.UNAUTHORIZED)
//					.body(ApiResponse.error(401, "登入失敗:"+e.getMessage()));
//		}
//	}
private static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);
	@PostMapping("/login")
	ResponseEntity<ApiResponse<LoginResponse>> login(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String captchaId,
			@RequestParam String captchaInput) {
		logger.info("!!!!!!!!!! LOGIN API HAS BEEN CALLED !!!!!!!!!!");
		String redisKey="captchaId:"+captchaId;
		String correctCaptcha=redisTemplate.opsForValue().get(redisKey);
		if(correctCaptcha==null|| !correctCaptcha.equals(captchaInput)){
			return  ResponseEntity.badRequest().body(ApiResponse.error(400,"驗證碼輸入錯誤"));
		}
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password)
			);
			UserCert userCert=userCertService.findUserByUsername(username);
			if(userCert.isIsbanned()==true){
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(ApiResponse.error(403, "此帳號已被封鎖，禁止登入"));
			}
			final String jwt=jwtUtils.generationToken(userCert);
			LoginResponse loginResponse=new LoginResponse();
			loginResponse.setToken(jwt);
			return ResponseEntity.ok(ApiResponse.success("登入成功", loginResponse));
		} catch (BadCredentialsException e){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401,"登入失敗，帳號密碼錯誤"));
		}
		catch (UserException e) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(401, "登入失敗:"+e.getMessage()));
		}
	}
	@GetMapping("/logout")
	public ResponseEntity<ApiResponse<Void>>Logout(){
//		if(session.getAttribute("userCert")==null) {
//			return ResponseEntity
//					.status(HttpStatus
//					.UNAUTHORIZED)
//					.body(ApiResponse
//							.error(401, "尚未登入，登出失敗"));
//		}
//		session.invalidate();
		return ResponseEntity.ok(ApiResponse.success("登出成功", null));
	}
	@GetMapping("/check-login")
	public ResponseEntity<ApiResponse<Boolean>> checkLogin(HttpSession session) {
//	    boolean loggedIn = session.getAttribute("userCert") != null;
	    return ResponseEntity.ok(ApiResponse.success("檢查登入", true));
	}
	
	@GetMapping("/userinfo")
	public ResponseEntity<ApiResponse<UserCert>>userInfo(HttpSession session){
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication+"=======================");
		if(authentication==null||!authentication.isAuthenticated()){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(401, "使用者未登入或身份驗證失敗"));
		}
		String username=authentication.getName();
		UserCert userCert=userCertService.findUserByUsername(username);
		if (userCert == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(ApiResponse.error(404, "找不到該使用者的資訊"));
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
	@PutMapping("/user/block/{id}")
	public ResponseEntity<ApiResponse<Void>>blockUser(@PathVariable Long id){
		try{
			userService.BlockUser(id);
			return ResponseEntity.ok(ApiResponse.success("封鎖成功",null));
		}catch (UserNotFoundException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
		}
	}
	@PutMapping("/user/unblock/{id}")
	public ResponseEntity<ApiResponse<Void>>unblockUser(@PathVariable Long id){
		try{
			userService.unBlockUser(id);
			return ResponseEntity.ok(ApiResponse.success("解除封鎖成功",null));
		}catch (UserNotFoundException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
		}
	}
	
}
