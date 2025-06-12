package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.model.entity.ChatRoom;

public interface ChatMessageService {
	ChatMessageDto saveMessage(Long senderId,ChatRoom chatRoom,String content
			);
	List<ChatMessageDto> getMessagesByChatRoom(Long chatRoomId);
}
