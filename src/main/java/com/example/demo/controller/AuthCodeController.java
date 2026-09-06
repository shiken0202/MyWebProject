package com.example.demo.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.example.demo.model.dto.CaptchaDto;
import com.example.demo.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
public class AuthCodeController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@GetMapping("/captcha")
	public ResponseEntity<ApiResponse<CaptchaDto>> authcode() throws IOException{
		String authcode=generateAuthCode();
		String captchaId= UUID.randomUUID().toString();
		String redisKey="captchaId:"+captchaId;
		// 為了方便 Insomnia 測試，直接印出驗證碼在 Console
		System.out.println("====== [測試用] 剛產生的驗證碼是：" + authcode + " (Captcha ID: " + captchaId + ") ======");
		redisTemplate.opsForValue().set(redisKey,authcode,2, TimeUnit.MINUTES);
		BufferedImage AuthCodeImage =getAuthCodeImage(authcode);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			ImageIO.write(AuthCodeImage,"JPG",baos);
			byte[]bytes=baos.toByteArray();
			String imgBase64= Base64.getEncoder().encodeToString(bytes);
			CaptchaDto captchaDto=new CaptchaDto(captchaId,imgBase64);
			return ResponseEntity.ok(ApiResponse.success("驗證碼生成成功",captchaDto));
			
		} catch (IOException e) {
			e.printStackTrace();
		    return ResponseEntity.badRequest().body(ApiResponse.error(400,"驗證碼生成失敗："+e.getMessage()));
		}
		
	}
	
	private String generateAuthCode() {
		String chars = "123456789abcdefghijkmnpqrstuvwyABCDEFGHJKLMNPQRSTUVWY";
//		String chars ="0"; 測試用
		StringBuffer authcode = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<4;i++) {
			int index = random.nextInt(chars.length()); // 隨機取位置
			authcode.append(chars.charAt(index)); // 取得該位置的資料
			}
		return authcode.toString();
		}
	// 利用 Java2D 產生動態圖像
	private BufferedImage getAuthCodeImage(String authcode) {
		Random random=new Random();
		
		// 建立圖像區域(80x30 TGB)
		BufferedImage img = new BufferedImage(80, 30, BufferedImage.TYPE_INT_RGB);
		// 建立畫布
		Graphics g = img.getGraphics();
		// 設定顏色
		g.setColor(Color.YELLOW);
		// 塗滿背景
		g.fillRect(0, 0, 80, 30); // 全區域
		// 設定顏色
		g.setColor(Color.BLACK);
		// 設定字型
		g.setFont(new Font("Arial", Font.BOLD, 22)); // 字體, 風格, 大小
		// 繪文字
		g.drawString(authcode, 18, 22); // (18, 22) 表示繪文字左上角的起點
		//加上干擾線
		g.setColor(Color.MAGENTA);
		for(int i=0;i<15;i++) {
			int x1=random.nextInt(80);
			int y1=random.nextInt(30);
			int x2=random.nextInt(80);
			int y2=random.nextInt(30);
		g.drawLine(x1, y1, x2, y2);
		}
		
		
		return img;
		
	}
}
