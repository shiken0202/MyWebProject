package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ChatMessageDto;

import com.example.demo.model.entity.ChatMessage;


@Component
public class ChatMessageMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ChatMessageDto toDto(ChatMessage chatmessage) {
		ChatMessageDto chatMessageDto=modelMapper.map(chatmessage, ChatMessageDto.class);
		chatMessageDto.setSenderName(chatmessage.getSender().getUserName());
		
		return chatMessageDto;
	}
	
	public ChatMessage toEntity(ChatMessageDto chatMessageDto) {
		// DTO 轉 Entity
		return modelMapper.map(chatMessageDto, ChatMessage.class);
	}
	
}
