package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.ChatMessageDto;

public interface ChatMessageService {
	void sendMessage(Long chatRoomId, Long senderId, String content);
	List<ChatMessageDto> getMessagesByChatRoom(Long chatRoomId);
}
