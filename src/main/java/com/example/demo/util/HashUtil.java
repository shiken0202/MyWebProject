package com.example.demo.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {
	// 產生隨機鹽
	public static String generateSalt() throws Exception {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16]; // 16 bytes = 128 bits
		random.nextBytes(salt);
		return bytesToHex(salt);
	}
	
	// 產生雜湊
	public static String hashPassword(String password, String salt) throws Exception {
		// 使用 SHA-256 雜湊演算法
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = md.digest((password+salt).getBytes()); // **密碼與鹽混合加密
		return bytesToHex(hashBytes);
	}
	
	// 把 byte[] 轉十六進位字串
	private static String bytesToHex(byte[] bytes) {
		// 將 byte[] 轉成16進位字串
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	public static String getHash(String password) {
		try {
			// 加密演算法: SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// 進行加密
			byte[] bytes = md.digest(password.getBytes());
			//System.out.println(Arrays.toString(bytes));
			// 將 byte[] 透過 Base64 編碼方便儲存
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
