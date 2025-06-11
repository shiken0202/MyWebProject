package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.ChatRoomDto;

public interface ChatRoomService {
	void createChatRoom(Long buyerId,  Long storeId);
	List<ChatRoomDto> getAllChatRooms();
	
}
