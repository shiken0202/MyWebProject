package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ChatRoomNotFoundException;
import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.ChatMessageMapper;
import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.model.entity.ChatMessage;
import com.example.demo.model.entity.ChatRoom;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatMessageService;
import com.example.demo.service.ChatRoomService;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private ChatMessageMapper chatMessageMapper;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired 
	private ChatRoomService chatRoomService;
	@Autowired StoreRepository storeRepository;
	@Override
	public ChatMessageDto saveMessage(Long senderId,ChatRoom chatRoom,String content
			) {
		User sender=userRepository.findById(senderId).orElseThrow(()->new UserNotFoundException("找不到使用者"));
	
		
	
		ChatMessage chatMessage=new ChatMessage();
		chatMessage.setChatRoom(chatRoom);
		chatMessage.setSender(sender);
		chatMessage.setContent(content);
		chatMessageRepository.save(chatMessage);
		return chatMessageMapper.toDto(chatMessage);
	}

	@Override
	public List<ChatMessageDto> getMessagesByChatRoom(Long chatRoomId) {
		ChatRoom chatRoom=chatRoomRepository.findById(chatRoomId).orElseThrow(()->new ChatRoomNotFoundException("找不到聊天室"));
		List<ChatMessage> chatMessages=chatMessageRepository.findByChatRoom(chatRoom);
		return chatMessages.stream().map(chatMessageMapper::toDto).collect(Collectors.toList());
	}
	
}
