package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
	 private Long id;
	    private Long chatRoomId;       
	    private Long senderId;         
	    private Long buyerId;
	    private Long storeId;
	    private String content;        
	            
	    private Boolean isRead;        
	    private LocalDateTime createdAt; 
	    private String senderName;     
	    
}
