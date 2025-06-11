package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.service.ChatMessageService;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	@Override
	public void sendMessage(Long chatRoomId, Long senderId, String content
			) {
		// TODO 自動產生的方法 Stub
	
	}

	@Override
	public List<ChatMessageDto> getMessagesByChatRoom(Long chatRoomId) {
		// TODO 自動產生的方法 Stub
		return null;
	}
	
}
