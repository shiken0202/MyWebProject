package com.example.demo.controller;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.LoginResponse;
import com.example.demo.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "使用者認證 (Authentication)", description = "提供使用者登入、註冊與資訊查詢等相關 API")
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
	@Operation(summary = "使用者登入",description = "供帳號、密碼、驗證碼比對來進行登入，成功後回傳 Token。")
	ResponseEntity<ApiResponse<LoginResponse>> login(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String captchaId,
			@RequestParam String captchaInput) {
		logger.info("!!!!!!!!!! LOGIN API HAS BEEN CALLED !!!!!!!!!!");
		String redisKey="captchaId:"+captchaId;
		String correctCaptcha=redisTemplate.opsForValue().get(redisKey);
		if (correctCaptcha != null) {
			redisTemplate.delete(redisKey);
		}
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
			final String accessToken = jwtUtils.generateAccessToken(userCert);
			final String refreshToken = jwtUtils.generateRefreshToken(userCert);
			
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setAccessToken(accessToken);
			loginResponse.setRefreshToken(refreshToken);
			
			return ResponseEntity.ok(ApiResponse.success("登入成功", loginResponse));
		} catch (BadCredentialsException e){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401,"登入失敗，帳號密碼錯誤"));
		} catch (UserException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, "登入失敗:"+e.getMessage()));
		}
	}

	@PostMapping("/refresh")
	@Operation(summary = "換發 Access Token", description = "當短 Token 過期時，利用長 Token (Refresh Token) 來換發新的短 Token。")
	ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam String refreshToken) {
		logger.info("!!!!!!!!!! REFRESH API HAS BEEN CALLED !!!!!!!!!!");
		
		// 1. 驗證 Refresh Token 是否合法且未過期
		if (!jwtUtils.validateRefreshToken(refreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(401, "Refresh Token 無效或已過期，請重新登入"));
		}

		try {
			// 2. 從 Token 中解析出 username
			String username = jwtUtils.extractUsername(refreshToken);
			
			// 3. 重新從資料庫獲取使用者最新狀態
			UserCert userCert = userCertService.findUserByUsername(username);
			if(userCert.isIsbanned()){
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(ApiResponse.error(403, "此帳號已被封鎖，禁止換發憑證"));
			}

			// 4. 核發一張全新的 Access Token (為了安全起見，通常我們也會連 Refresh Token 一起換新發布，這稱為 Refresh Token Rotation，但先以換發 Access Token 為主即可)
			String newAccessToken = jwtUtils.generateAccessToken(userCert);
			String newRefreshToken = jwtUtils.generateRefreshToken(userCert); // 視需求可給舊的，這裡選擇給新的刷新壽命

			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setAccessToken(newAccessToken);
			loginResponse.setRefreshToken(newRefreshToken);

			return ResponseEntity.ok(ApiResponse.success("換發成功", loginResponse));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(401, "換發憑證失敗: " + e.getMessage()));
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
	@Operation(summary = "確認使用者登入狀況", description = "需要有效的 Token 才能訪問。")
	public ResponseEntity<ApiResponse<Boolean>> checkLogin(HttpSession session) {
//	    boolean loggedIn = session.getAttribute("userCert") != null;
	    return ResponseEntity.ok(ApiResponse.success("檢查登入", true));
	}
	
	@GetMapping("/userinfo")
	@Operation(summary = "取得使用者資訊", description = "需要有效的 Token 才能訪問。")
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
	@Operation(summary = "封鎖指定使用者")
	@ApiResponses(
			value = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(
							responseCode = "200",
							description = "封鎖成功",
							content = @Content(mediaType = "application/json",
											schema = @Schema(implementation = com.example.demo.response.ApiResponse.class
													   ),
											examples = @ExampleObject(
													name = "successExample", // 給這個範例取一個名字
													summary = "成功回應範例", // 範例的簡短描述
													value = "{ \"status\": 200, \"message\": \"操作成功\", \"data\": null }" // 實際的 JSON 範例字串
											)

							)
					),
					@io.swagger.v3.oas.annotations.responses.ApiResponse(
							responseCode = "400",
							description = "封鎖失敗,使用者不存在",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = com.example.demo.response.ApiResponse.class
													   ),
									examples = @ExampleObject(
										name = "successExample", // 給這個範例取一個名字
										summary = "失敗回應範例", // 範例的簡短描述
										value = "{ \"status\": 400, \"message\": \"操作失敗\", \"data\": null }" // 實際的 JSON 範例字串
										)
							)
					)
			}
	)

	public ResponseEntity<ApiResponse<Void>>unblockUser(@Parameter(description = "封鎖的使用者ID",example = "6") @PathVariable Long id){
		try{
			userService.unBlockUser(id);
			return ResponseEntity.ok(ApiResponse.success("解除封鎖成功",null));
		}catch (UserNotFoundException e) {
			return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
		}
	}
	
}
