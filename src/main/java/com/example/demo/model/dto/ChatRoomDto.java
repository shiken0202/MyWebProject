package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
	private Long id;
    private Long buyerId;      // 買家 user ID
    private Long sellerId;     // 賣家 user ID
    private Long storeId;      // 商店 ID
    
    private LocalDateTime createdAt; // 創建時間
    private String buyerName;  
    private String storeName;
    private String productName;
}
