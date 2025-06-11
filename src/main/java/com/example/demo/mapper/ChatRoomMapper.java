package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ChatRoomDto;

import com.example.demo.model.entity.ChatRoom;

@Component
public class ChatRoomMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ChatRoomDto toDto(ChatRoom chatRoom) {
		ChatRoomDto chatRoomDto=modelMapper.map(chatRoom, ChatRoomDto.class);
		chatRoomDto.setBuyerName(chatRoom.getBuyer().getUserName());
		chatRoomDto.setStoreName(chatRoom.getStore().getStoreName());
		
		return chatRoomDto;
	}
	
	public ChatRoom toEntity(ChatRoomDto chatRoomDto) {
		// DTO 轉 Entity
		return modelMapper.map(chatRoomDto, ChatRoom.class);
	}
	
}
