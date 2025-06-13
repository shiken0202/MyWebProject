package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.ChatRoomDto;
import com.example.demo.model.entity.ChatRoom;

public interface ChatRoomService {
	ChatRoom createChatRoom(Long buyerId,  Long storeId);
	List<ChatRoomDto> getAllChatRooms(Long userId);
	
}
