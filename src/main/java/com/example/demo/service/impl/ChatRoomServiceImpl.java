package com.example.demo.service.impl;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.ChatRoomMapper;
import com.example.demo.model.dto.ChatRoomDto;
import com.example.demo.model.entity.ChatRoom;

import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatRoomService;
@Service
public class ChatRoomServiceImpl implements ChatRoomService {
	
@Autowired
private UserRepository userRepository;
@Autowired
private StoreRepository storeRepository;
@Autowired
private ChatRoomRepository chatRoomRepository;
@Autowired
private ChatRoomMapper chatRoomMapper;

	@Override
	public ChatRoom createChatRoom(Long buyerId, Long storeId) {
		User buyer=userRepository.findById(buyerId).orElseThrow(()->new UserNotFoundException("找不到此用戶"));
		Store store=storeRepository.findById(storeId).orElseThrow(()->new StoreNotFoundException("找不到商家"));
		ChatRoom chatroom=new ChatRoom();
		chatroom.setBuyer(buyer);
		chatroom.setStore(store);
		chatroom.setSeller(store.getUser());
		chatRoomRepository.save(chatroom);
		return chatroom;
		
	}

	@Override
	public List<ChatRoomDto> getAllChatRooms(Long userId) {
		if(userRepository.findById(userId).isEmpty()){
			throw  new UserNotFoundException("找不到使用者");
		}
		List<ChatRoom> chatRooms=chatRoomRepository.findAllChatRoomsByUserId(userId);
		return chatRooms.stream().map(chatRoomMapper::toDto).collect(Collectors.toList());
	}

	
	
}
